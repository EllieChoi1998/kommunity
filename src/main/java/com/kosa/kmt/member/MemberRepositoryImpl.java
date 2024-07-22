package com.kosa.kmt.member;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Integer memberId) {
        Member member = em.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {

        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {

        List<Member> result = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public Member update_nickname(Member member, String nickname) {
        member.setNickname(nickname);
        em.merge(member);
        return member;
    }

    @Override
    public Member update_password(Member member, String password) {
        member.setPassword(password);
        em.merge(member);
        return member;
    }

    @Override
    public Member updateExtendLogin(Member member, String change) {
        member.setExtendLogin(change);
        em.merge(member);
        return member;
    }


    @Override
    public Member updateLoginTime(Member member, LocalDateTime loginTime){
        member.setLoginTime(loginTime);
        em.merge(member);
        return member;
    }

    @Override
    public Member updateLogoutTime(Member member, LocalDateTime logoutTime){
        member.setLogoutTime(logoutTime);
        em.merge(member);
        return member;
    }

    @Override
    public Member delete(Member member) {
        return null;
    }
}


