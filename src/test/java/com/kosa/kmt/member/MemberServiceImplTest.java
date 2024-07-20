package com.kosa.kmt.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @BeforeEach
    void setUp() {
        // tester user
        Member member = new Member();
        member.setName("test");
        member.setEmail("test@test.com");
        memberRepository.save(member);
    }

    @Test
    // @Commit
    public void 회원가입() throws Exception {
        //Given

        String input_email = "test@test.com";
        String input_name = "test";

        Member member = null;
        Integer savedId = -1;
        //When
        Integer userId = memberService.findSameEmail(input_email);
        if (userId != -1){
            member = memberRepository.findById(userId).get();
            Integer matchedNameUserId = memberService.findSameName(input_name);
            if(matchedNameUserId == member.getMemberId()){
                memberService.updateNickname(member, "tester user");
                memberService.updatePassword(member, "tester password");
                savedId = memberService.save(member);
            }
        }

        //Then
        Member findMember = memberRepository.findById(savedId).get();
        assertEquals(member.getName(), findMember.getName());
    }
}