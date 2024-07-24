package com.kosa.kmt.member;

public interface MemberService {
    Integer login(String email, String password) throws Exception;
    Boolean logout(Member member);

    Boolean updateAuthEmail(Member member, String authEmail);

    Integer findSameName(String name);
    Integer findSameEmail(String email);

    Boolean ExtendLogin(Member member);
    Boolean CancelExtendLogin(Member member);

    Boolean updateNickname(Member member, String nickname);
    Boolean updatePassword(Member member, String password);

    Member saveMember(String name, String email, String password);

    String sendCodeToEmail(String email);
}
