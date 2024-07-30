package com.kosa.kmt.controller;


import com.kosa.kmt.nonController.chat.Chat;
import com.kosa.kmt.nonController.chat.ChatDTO;
import com.kosa.kmt.nonController.chat.ChatService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/kommunity/chat/getAllChats")
    public ResponseEntity<Map<String, Object>> getAllChats() {
        List<Chat> chats = chatService.findAllChats();
        Member currentMember = getCurrentMember();
        List<ChatDTO> chatDTOs = new ArrayList<>();

        for (Chat chat : chats) {
            boolean isCurrentUser = chat.getMember().equals(currentMember);
            System.out.println("isCurrentUser: " + isCurrentUser);
            ChatDTO chatDTO = new ChatDTO(
                    chat.getChatContent(),
                    chat.getChatDateTime(),
                    chat.getMember().getMemberId(),
                    chat.getMember().getName(), // 보낸 유저의 이름 설정
                    isCurrentUser
            );
            chatDTOs.add(chatDTO);

            System.out.println(chatDTO.getIsCurrentUser());
        }

        chatDTOs.sort(Comparator.comparing(ChatDTO::getChatDateTime));

        Map<String, Object> response = new HashMap<>();
        response.put("chatContents", chatDTOs);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/kommunity/chat/sendChat")
    public ResponseEntity<Map<String, Object>> sendChat(@RequestBody Map<String, String> chatRequest) throws Exception {
        String content = chatRequest.get("content");

        if (content != null && !content.trim().isEmpty()) {
            Member currentMember = getCurrentMember();

            if (currentMember == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Unauthorized"));
            }

            Chat chat = new Chat();
            chat.setChatContent(content);
            chat.setChatDateTime(LocalDateTime.now());
            chat.setMember(currentMember);

            Long savedChatId = chatService.saveChat(chat, currentMember.getMemberId());
            System.out.println("Saved: " + savedChatId);
            if (savedChatId != -1) {
                ChatDTO chatDTO = new ChatDTO(
                        chat.getChatContent(),
                        chat.getChatDateTime(),
                        chat.getMember().getMemberId(),
                        chat.getMember().getName(),
                        true
                );

                Map<String, Object> response = new HashMap<>();
                response.put("chatContent", chatDTO);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to save chat"));
            }
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid content"));
        }
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
}
