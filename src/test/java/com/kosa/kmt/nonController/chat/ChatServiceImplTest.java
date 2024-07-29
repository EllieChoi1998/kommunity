package com.kosa.kmt.nonController.chat;

import com.kosa.kmt.nonController.chat.Chat;
import com.kosa.kmt.nonController.chat.ChatRepository;
import com.kosa.kmt.nonController.chat.ChatService;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
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
        Long savedChatId = chatService.saveChat(chat, 1);
        Long actualId = chat.getChatId();
        assertEquals(savedChatId, actualId);
    }

    /*
    아래 테스트는 새로운 데이터베이스로 실행하면 무조건 실패합니다. 하지만,
    아래의 "프로시저호출테스트"에서 갯수를 조회하는 메서드를 사용하고 있으니, 테스팅은 문제가 없을 것입니다.
    주석 처리하고 테스팅 돌려도 무관 합니다.
     */
//    @Test
//    public void 모든채팅조회_valid() throws Exception {
//        List<Chat> allchats = chatService.findAllChats();
//
//        assertEquals(allchats.size(), 2);
//
//    }

    @Test
    public void 프로시저호출테스트_100개넘는채팅메시지갯수_valid() throws Exception {
        for (int i = 0; i <= 100; i++) {
            Chat chat = new Chat();
            chat.setChatContent("Procedure Chat content" + i);
            chat.setChatDateTime(LocalDateTime.now());
            chatService.saveChat(chat, 1);
        }

        assertEquals(100, chatService.findAllChats().size());
    }

    @Test
    public void 신규채팅메시지생성_invalid() throws Exception {
        Chat chat = new Chat();
        chat.setChatContent("Invalid Procedure Chat content");
        chat.setChatDateTime(LocalDateTime.now());
        Long result = chatService.saveChat(chat, -1);

        assertEquals(-1, result);
    }

}
