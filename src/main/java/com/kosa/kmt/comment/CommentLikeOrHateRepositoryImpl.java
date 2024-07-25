package com.kosa.kmt.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class CommentLikeOrHateRepositoryImpl implements CommentLikeOrHateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addLike(Long commentId, Long memberId) {
        CommentLike like = new CommentLike();
        like.setPostComment(entityManager.find(PostComment.class, commentId));
        like.setMemberId(memberId);
        entityManager.persist(like);
    }

    @Override
    @Transactional
    public void addHate(Long commentId, Long memberId) {
        CommentHate hate = new CommentHate();
        hate.setPostComment(entityManager.find(PostComment.class, commentId));
        hate.setMemberId(memberId);
        entityManager.persist(hate);
    }

    @Override
    @Transactional
    public void removeLike(Long commentId, Long memberId) {
        entityManager.createQuery("DELETE FROM CommentLike l WHERE l.postComment.comment_Id = :commentId AND l.memberId = :memberId")
                .setParameter("commentId", commentId)
                .setParameter("memberId", memberId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeHate(Long commentId, Long memberId) {
        entityManager.createQuery("DELETE FROM CommentHate h WHERE h.postComment.comment_Id = :commentId AND h.memberId = :memberId")
                .setParameter("commentId", commentId)
                .setParameter("memberId", memberId)
                .executeUpdate();
    }

    @Override
    public long countLikesByCommentId(Long commentId) {
        return entityManager.createQuery("SELECT COUNT(l) FROM CommentLike l WHERE l.postComment.comment_Id = :commentId", Long.class)
                .setParameter("commentId", commentId)
                .getSingleResult();
    }

    @Override
    public long countHatesByCommentId(Long commentId) {
        return entityManager.createQuery("SELECT COUNT(h) FROM CommentHate h WHERE h.postComment.comment_Id = :commentId", Long.class)
                .setParameter("commentId", commentId)
                .getSingleResult();
    }
}