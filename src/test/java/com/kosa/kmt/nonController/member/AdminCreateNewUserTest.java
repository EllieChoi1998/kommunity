package com.kosa.kmt.nonController.member;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class AdminCreateNewUserTest {

    @Autowired
    MemberRepository memberRepository;
    @Test
    public void test() {
//        Optional<Member> memberls = memberRepository.findByName("admin");
//        if (memberls.isPresent() == false) {
            Member member = new Member();
            member.setEmail("admin@admin.com");
            member.setName("admin");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode("admin");
            member.setPassword(encodedPassword);
            memberRepository.save(member);
//        }
    }

    @Test
    public void test2() {
//        Optional<Member> memberls = memberRepository.findByName("admin");
//        if (memberls.isPresent() == false) {
        Member member = new Member();
        member.setEmail("onestone1380@email.com");
        member.setName("장원석");

        memberRepository.save(member);
//        }
    }

    @Test
    public void test3() {
//        Optional<Member> memberls = memberRepository.findByName("admin");
//        if (memberls.isPresent() == false) {
        Member member = new Member();
        member.setEmail("teamkom2024@gmail.com");
        member.setName("최혜령");

        memberRepository.save(member);
//        }
    }
}
