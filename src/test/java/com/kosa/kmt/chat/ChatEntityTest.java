package com.kosa.kmt.chat;

import com.kosa.kmt.non_controller.chat.Chat;
import com.kosa.kmt.non_controller.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ChatEntityTest {
    private Chat chat;
    private Member member;

    @BeforeEach
    public void setUp() {
        this.chat = new Chat();
        this.chat.setChatContent("Chat Entity Test");
        this.chat.setChatDateTime(LocalDateTime.now());
        this.member = new Member();
        this.member.setName("Chat Entity Member");
        this.member.setEmail("chatentitymember@gmail.com");
        this.chat.setMember(this.member);
    }

    @Test
    public void getContentTest(){
        String result = this.chat.getChatContent();
        String actual = "Chat Entity Test";

        assertEquals(result, actual);
    }

    @Test
    public void getDateTimeTest(){
        Integer result = this.chat.getChatDateTime().getHour();
        Integer expected = LocalDateTime.now().getHour();
        assertEquals(result, expected);
    }

    @Test
    public void getMemberTest() {
        Integer expected = this.member.getMemberId();
        Integer result = this.chat.getMember().getMemberId();
        assertEquals(result, expected);
    }
}
