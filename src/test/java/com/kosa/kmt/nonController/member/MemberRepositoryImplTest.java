package com.kosa.kmt.nonController.member;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberRepositoryImplTest {
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
        this.member = this.memberRepository.save(this.member);
    }

    @Test
    public void testfindAll(){
        List<Member> members = memberRepository.findAll();
        System.out.println("members : " + members.size());
        assertEquals(2, members.size());
    }

//    @Test
//    public void testSave_invalid(){
//        Member savedMember = this.memberRepository.save(this.member);
//        assertEquals(this.member.getName(), savedMember.getName());
//    }

    @Test
    public void testUpdateNickname(){
        // Given
        String newNickname = "Ellie Choi";
        // When
        Member result = memberRepository.update_nickname(member, newNickname);

        //Then
        assertEquals(newNickname, memberRepository.findById(result.getMemberId()).get().getNickname());

    }
}
