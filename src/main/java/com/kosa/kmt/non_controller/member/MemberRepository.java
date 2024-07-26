package com.kosa.kmt.non_controller.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Integer memberId);

    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String name);
    List<Member> findAll();
    Member update_nickname(Member member, String nickname);
    Member update_password(Member member, String password);

    Member updateExtendLogin(Member member, String change);

    Member updateLoginTime(Member member, LocalDateTime loginTime);

    Member updateLogoutTime(Member member, LocalDateTime logoutTime);

    Member updateAuthEmail(Member member, String authEmail);

    Member delete(Member member);
}