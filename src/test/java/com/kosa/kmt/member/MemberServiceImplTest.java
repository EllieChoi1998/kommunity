package com.kosa.kmt.member;

import com.kosa.kmt.non_controller.member.Member;
import com.kosa.kmt.non_controller.member.MemberRepository;
import com.kosa.kmt.non_controller.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    Member member;
    @BeforeEach
    void setUp() {
        // tester user
        this.member = new Member();
        this.member.setName("test1");
        this.member.setEmail("test1@test.com");
        this.memberRepository.save(this.member);
    }

    @Test
    // @Commit
    public void 회원가입과로그인_valid() throws Exception {
        //Given

        String input_email = "test1@test.com";
        String input_name = "test1";

        Member newmember = null;
        Boolean successNickname = false;
        Boolean successPassword = false;
        //When
        Integer userId = memberService.findSameEmail(input_email);
        if (userId != -1){
            newmember = memberRepository.findById(userId).get();
            Integer matchedNameUserId = memberService.findSameName(input_name);
            if(matchedNameUserId == newmember.getMemberId()){
                successNickname = memberService.updateNickname(newmember, "user");
                successPassword = memberService.updatePassword(newmember, "tester password");

            }
        }

        //Then
        assertEquals(true, successNickname);
        assertEquals(true, successPassword);

        String password = "tester password";
        Integer loginMemberId = memberService.login(input_email, password);
        assertEquals(this.member.getMemberId(), loginMemberId);
    }

    @Test
    public void testExtendLogin(){
        Boolean result = this.memberService.ExtendLogin(this.member);
        assertEquals(true, result);
        assertEquals("T", this.member.getExtendLogin());
    }

    @Test
    public void testCancelLogin(){
        this.memberService.ExtendLogin(this.member);
        Boolean result = this.memberService.CancelExtendLogin(this.member);
        assertEquals(true, result);
        assertEquals("F", this.member.getExtendLogin());
    }

    @Test
    public void testLogout(){
        this.memberService.logout(this.member);
        assertEquals(LocalDateTime.now().getHour(), this.member.getLogoutTime().getHour());
    }
}