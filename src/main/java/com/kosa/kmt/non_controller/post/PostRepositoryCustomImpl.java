package com.kosa.kmt.non_controller.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Post> findAllOrderByBookmarksDesc() {
        String jpql = "SELECT p FROM Post p LEFT JOIN p.bookMarks b GROUP BY p ORDER BY COUNT(b) DESC";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class);
        return query.getResultList();
    }

    @Override
    public List<Post> findAllOrderByCommentsDesc() {
        String jpql = "SELECT p FROM Post p LEFT JOIN p.comments c GROUP BY p ORDER BY COUNT(c) DESC";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class);
        return query.getResultList();
    }
}
