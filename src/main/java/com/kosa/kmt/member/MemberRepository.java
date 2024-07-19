package com.kosa.kmt.member;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member) throws SQLException;
    Optional<Member> findById(Integer id);
//    Optional<Member> findByName(String name);
//
//    Optional<Member> findByEmail(String name);
//
//    List<Member> findAll();
}