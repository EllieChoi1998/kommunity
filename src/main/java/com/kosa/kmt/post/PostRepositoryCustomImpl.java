package com.kosa.kmt.post;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final EntityManager em;

//    @Override
//    public LocalDateTime findPostDateById(Long id) {
//        String jpql = "SELECT p.postDate FROM Post p WHERE p.id = :id";
//        return em.createQuery(jpql, LocalDateTime.class)
//                .setParameter("id", id)
//                .getSingleResult();
//    }
}
