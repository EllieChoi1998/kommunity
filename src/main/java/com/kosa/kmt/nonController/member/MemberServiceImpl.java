package com.kosa.kmt.nonController.member;

import com.kosa.kmt.exceptions.DataNotFoundException;
import com.kosa.kmt.nonController.member.signup.email.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    private MailService mailService;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    return 1 = Login Success
    return 0 = update loginTime failed
    reuturn -1 = password unmatched
    return -2 = email not found
     */
    @Override
    public Integer login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()){
            Member currentMember = member.get();
            String userPassword = currentMember.getPassword();
            if (password.equals(userPassword)){
                Member updatedmember = memberRepository.updateLoginTime(currentMember, LocalDateTime.now());
                if (updatedmember != null){
                    return updatedmember.getMemberId();
                }
                return 0;
            }
            return -1;
        }
        return -2;
    }

    @Override
    public Boolean logout(Member member) {
        Member updatedmember = memberRepository.updateLogoutTime(member, LocalDateTime.now());
        if (updatedmember != null){
            return true;
        }
        return false;
    }

    /*
    Signup must be called at the very last stage in the Controller.
    For signup, there's three stages:
        1. call findSameEmail() -- should return true
        2. call findSameName() -- should return true
        3. call updateAuthEmail() --> call login

     */

    @Override
    public Boolean updateAuthEmail(Member member, String authEmail){
        Member updatedMember = memberRepository.updateAuthEmail(member, authEmail);
        if (updatedMember != null){
            return true;
        }
        return false;
    }

    /*
    로그인을 위한 이메일 및 이름 찾기 입니다.
    findSameName, findSameEmail 둘다 같은 로직으로,
    각 값을 가진 멤버 데이터를 찾으면 returns memberId.
    (폐쇄형 애플리케이션 특성 상, 멤버들의 이메일과 이름은 이미 데이터에 저장되어 있습니다).
     */
    @Override
    public Integer findSameName(String name) {
        Integer foundId = memberRepository.findByName(name).get().getMemberId();
        if(foundId != null) {
            return foundId;
        } else {
            return -1;
        }
    }
    @Override
    public Integer findSameEmail(String email) {
        Integer foundId = memberRepository.findByEmail(email).get().getMemberId();
        if(foundId != null) {
            return foundId;
        } else {
            return -1;
        }
    }


    @Override
    public Boolean ExtendLogin(Member member) {
        Member updatedMember = memberRepository.updateExtendLogin(member, "T");
        if(updatedMember != null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean CancelExtendLogin(Member member) {
        Member updatedMember = memberRepository.updateExtendLogin(member, "F");
        if(updatedMember != null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateNickname(Member member, String nickname) {
        Member updatedMember = memberRepository.update_nickname(member, nickname);
        if(updatedMember != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updatePassword(Member member, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode(password);
        Member updatedMember = memberRepository.update_password(member, pwd);
        if(updatedMember != null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updatePassword(Member member, String newPassword, String oldPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 사용자가 입력한 현재 비밀번호를 확인
        if (encoder.matches(oldPassword, member.getPassword())) {
            // 새로운 비밀번호를 인코딩하여 저장
            String encodedNewPassword = encoder.encode(newPassword);
            member.setPassword(encodedNewPassword);
            memberRepository.save(member);
            return true;
        }
        return false; // 현재 비밀번호가 일치하지 않을 경우
    }

    @Override
    public Member saveMember(String name, String email, String password){
        Member member = new Member();
        member.setEmail(email);
        member.setName(name);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        member.setPassword(encodedPassword);
        return member;
    }

    @Override
    public String sendCodeToEmail(String email) {
        Integer validEmail = this.findSameEmail(email);
        if (validEmail != -1){
            String title = "Kommunity 이메일 인증 번호";
            String authCode = this.createCode();
            mailService.sendEmail(email, title, authCode);
            return authCode;
        }
        return String.valueOf(validEmail);
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MemberService.createCode() exception occur");
            throw new DataNotFoundException("No such algorithm");
        }
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Member not found"));
    }

    @Override
    public void registerMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

//    @Autowired
//    private PasswordEncoder passwordEncoder; // PasswordEncoder 빈 주입 -> 중복이라서 실행이 X
//
//    @Override
//    public Boolean verifyPassword(Member member, String password) {
//        return passwordEncoder.matches(password, member.getPassword());
//    }
}
