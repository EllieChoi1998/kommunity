package com.kosa.kmt.chat;

import com.kosa.kmt.nonController.chat.Chat;
import com.kosa.kmt.nonController.chat.ChatRepository;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ChatRepositoryImplTest {

    @Autowired
    private ChatRepository chatRepository;


    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 프로시저호출테스트_100개넘는채팅메시지갯수() throws Exception {
        for (int i = 100; i < 220; i++) {
            Chat chat = new Chat();
            chat.setChatContent("Procedure Chat content" + i);
            chat.setChatDateTime(LocalDateTime.now());
            chat.setMember(memberRepository.findById(1).get());
            chatRepository.save(chat);
        }

        chatRepository.executeCleanup();

        assertEquals(100, chatRepository.findAll().size());
    }
}
