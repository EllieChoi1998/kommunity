package com.kosa.kmt.nonController.member;

import java.util.List;

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
    Boolean updatePassword(Member member, String newPassword, String oldPassword);

    Member saveMember(String name, String email, String password);

    String sendCodeToEmail(String email);

    // 추가된 메서드 07.30 김진석
    void save(Member member);
    Member findByEmail(String email);

    public void registerMember(Member member);

    List<Member> findAllMembers();

    void deleteMemberById(Integer memberId);

    boolean isEmailExists(String email);
}
