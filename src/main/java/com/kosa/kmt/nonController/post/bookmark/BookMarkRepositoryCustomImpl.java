package com.kosa.kmt.nonController.post.bookmark;

import com.kosa.kmt.nonController.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookMarkRepositoryCustomImpl implements BookMarkRepositoryCustom {

    private final EntityManager em;

    @Override
    public Long countBookMarksByPost(Post post) {
        String jpql = "SELECT COUNT(b) FROM BookMark b WHERE b.post.id = :postId";
        return em.createQuery(jpql, Long.class)
                .setParameter("postId", post.getId())
                .getSingleResult();
    }
}
