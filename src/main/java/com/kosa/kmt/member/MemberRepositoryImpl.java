package com.kosa.kmt.member;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Member save(Member member) throws SQLException {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Integer memberId) {
        Member member = em.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

//    @Override
//    public Optional<Member> findByName(String name) {
//
//        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
//                .setParameter("name", name)
//                .getResultList();
//
//        return result.stream().findAny();
//    }
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//
//        List<Member> result = em.createQuery("select m from Member m where m.email = :email", Member.class)
//                .setParameter("email", email)
//                .getResultList();
//
//        return result.stream().findAny();
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m", Member.class).getResultList();
//    }
}


