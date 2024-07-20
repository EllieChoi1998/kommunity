package com.kosa.kmt.member;

public interface MemberService {
    Integer login(String email, String password) throws Exception;
    Boolean logout(Member member);

    Integer save(Member member) throws Exception;

    Integer findSameName(String name);
    Integer findSameEmail(String email);

    Boolean ExtendLogin(Member member);
    Boolean CancelExtendLogin(Member member);

    Boolean updateNickname(Member member, String nickname);
    Boolean updatePassword(Member member, String password);
}
