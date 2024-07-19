package com.kosa.kmt.chat;

import com.kosa.kmt.member.Member;
import com.kosa.kmt.member.MemberRepository;
import com.kosa.kmt.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Transactional
public class ChatServiceImplTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void 신규채팅메시지생성_valid() throws Exception {

        Chat chat = new Chat();

        chat.setChatContent("Chat content3");
        chat.setChatDateTime(LocalDateTime.now());
        Chat savedChat = chatService.saveChat(chat, 1);

        assertEquals(chat.getChatId(), savedChat.getChatId());
    }

    @Test
    public void 모든채팅조회_valid() throws Exception {
        List<Chat> allchats = chatService.findAllChats();

        assertEquals(allchats.size(), 2);

    }

    @Test
    public void 프로시저호출테스트_100개넘는채팅메시지갯수() throws Exception {
        for (int i = 0; i <= 100; i++) {
            Chat chat = new Chat();
            chat.setChatContent("Procedure Chat content" + i);
            chat.setChatDateTime(LocalDateTime.now());
            chatService.saveChat(chat, 1);
        }

        assertEquals(100, chatService.findAllChats().size());
    }

}
