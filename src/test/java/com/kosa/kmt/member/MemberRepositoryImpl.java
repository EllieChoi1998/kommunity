package com.kosa.kmt.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
// @Transactional
public class MemberRepositoryImpl {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() {
        this.member = new Member();
        this.member.setName("John Doe");
        this.member.setEmail("john.doe@gmail.com");
        this.member.setNickname("JD");
        this.member.setPassword("1234");
    }

    @Test
    public void testSave(){
        Member savedMember = this.memberRepository.save(this.member);
        assertEquals(this.member.getName(), savedMember.getName());
    }

    @Test
    public void testUpdateNickname(){
        // Given
        String newNickname = "Ellie Choi";
        // When
        Member result = memberRepository.update_nickname(member, newNickname);

        //Then
        assertEquals(newNickname, result.getNickname());

    }
}
