package com.kosa.kmt.member;

public interface MemberService {
    void login(Member member) throws Exception;
    void setLoginTime(Member member);

    Integer join(Member member) throws Exception;
//    void logout(Member member);

//    void setLogoutTime(Member member);
//    void ExtendLogin(Member member);
//    void CancelExtendLogin(Member member);
//
//    void updateNickname(Member member, String nickname);
//    void updatePassword(Member member, String password);
}
