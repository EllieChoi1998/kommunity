package com.kosa.kmt.comment;

import com.kosa.kmt.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCommentRepositoryCustomImpl implements PostCommentRepositoryCustom {

    private final EntityManager em;

    @Override
    public Long countCommentsByPost(Post post) {
        String jpql = "SELECT COUNT(c) FROM PostComment c WHERE c.post.id = :postId";
        return em.createQuery(jpql, Long.class)
                .setParameter("postId", post.getId())
                .getSingleResult();
    }
}
