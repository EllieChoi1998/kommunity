package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.chat.Chat;
import com.kosa.kmt.nonController.chat.ChatDTO;
import com.kosa.kmt.nonController.chat.ChatService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static Member currentMember;

    @GetMapping("/kommunity/chat/getAllChats")
    public ResponseEntity<Map<String, Object>> getAllChats() {
        List<Chat> chats = chatService.findAllChats();
        currentMember = getCurrentMember();
        System.out.println("Current member: " + currentMember.getName());
        List<ChatDTO> chatDTOs = new ArrayList<>();

        for (Chat chat : chats) {
            boolean isCurrentUser = chat.getMember().equals(currentMember);
            ChatDTO chatDTO = new ChatDTO(
                    chat.getChatContent(),
                    chat.getChatDateTime(),
                    chat.getMember().getMemberId(),
                    chat.getMember().getName(),
                    isCurrentUser
            );
            chatDTOs.add(chatDTO);
        }

        chatDTOs.sort(Comparator.comparing(ChatDTO::getChatDateTime));

        Map<String, Object> response = new HashMap<>();
        response.put("chatContents", chatDTOs);

        return ResponseEntity.ok(response);
    }

    public ChatDTO sendChat(ChatDTO chatDTO, Member member) throws Exception {
        String content = chatDTO.getChatContent();

        if (content != null && !content.trim().isEmpty()) {

            if(member != null) {
                System.out.println(member.getEmail());
                System.out.println(member.getName());
                System.out.println(member.getNickname());

                Chat chat = new Chat();
                chat.setChatContent(content);
                chat.setChatDateTime(LocalDateTime.now());
                chat.setMember(member);

                Long savedChatId = chatService.saveChat(chat, member.getMemberId());
                if (savedChatId != -1) {
                    chatDTO.setChatWriterName(member.getName());
                    chatDTO.setChatWriterId(member.getMemberId());
                    chatDTO.setCurrentUser(true);
                    chatDTO.setChatDateTime(LocalDateTime.now());
                    sendMessageToWebSocket(chatDTO);
                }
            } else {
                System.out.println("NO MEMBER FOUND");
            }


        }
        return chatDTO;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatDTO sendMessage(@Payload ChatDTO chatMessage, Principal principal) throws Exception {
        String username = principal.getName();
        Member member = memberRepository.findByEmail(username).get();
        System.out.println("===========================================");
        System.out.println(username);
        System.out.println("===========================================");
        return this.sendChat(chatMessage, member);
    }

    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return memberRepository.findByEmail(userDetails.getUsername()).orElse(null);
            }
        }
        return null;
    }

    private void sendMessageToWebSocket(ChatDTO chatDTO) {
        messagingTemplate.convertAndSend("/topic/public", chatDTO);
    }
}
