package com.kosa.kmt.member;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void login(Member member) throws SQLException {
        //validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        setLoginTime(member);
    }
    @Override
    public void setLoginTime(Member member) {
        member.setLoginTime(LocalDateTime.now());
    }

    @Override
    public Integer join(Member member) throws SQLException {
        memberRepository.save(member);
        return member.getMemberId();
    }
//
//    private void validateDuplicateMember(Member member) {
//        memberRepository.findByName(member.getName())
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//                });
//    }

//    @Override
//    public void logout(Member member) {
//
//        setLogoutTime(member);
//    }
//

//
//    @Override
//    public void setLogoutTime(Member member) {
//        member.setLogout(LocalDateTime.now());
//
//    }
//
//    @Override
//    public void ExtendLogin(Member member) {
//        member.setExtendLogin("T");
//    }
//
//    @Override
//    public void CancelExtendLogin(Member member) {
//        member.setExtendLogin("F");
//    }
//
//    @Override
//    public void updateNickname(Member member, String nickname) {
//        member.setNickname(nickname);
//    }
//
//    @Override
//    public void updatePassword(Member member, String password) {
//        member.setPassword(password);
//    }


}
