package com.kosa.kmt.member;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Member updatedmember = em.createQuery("update Member m set m.nickname = :nickname where m.memberId = :memberId", Member.class)
                .setParameter("nickname", nickname)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
        return updatedmember;

    }

    @Override
    public Member update_pssword(Member member, String password) {
        Member updatedmember = em.createQuery("update Member m set m.password = :password where m.memberId = :memberId", Member.class)
                .setParameter("password", password)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
        return updatedmember;
    }

    @Override
    public Member updateExtendLogin(Member member, String change){
        Member updatedmember = em.createQuery("update Member m set m.extendLogin = :extendLogin where m.memberId = :memberId", Member.class)
                .setParameter("extendLogin", change)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
        return updatedmember;
    }

    @Override
    public Member updateLoginTime(Member member, LocalDateTime loginTime){
        Member updatedmember = em.createQuery("update Member m set m.loginTime = :loginTime where m.memberId = :memberId", Member.class)
                .setParameter("loginTime", loginTime)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
        return updatedmember;
    }

    @Override
    public Member updateLogoutTime(Member member, LocalDateTime logoutTime){
        Member updatedmember = em.createQuery("update Member m set m.logoutTime = :logoutTime where m.memberId = :memberId", Member.class)
                .setParameter("logoutTime", logoutTime)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
        return updatedmember;
    }

    @Override
    public Member delete(Member member) {
        return null;
    }
}


