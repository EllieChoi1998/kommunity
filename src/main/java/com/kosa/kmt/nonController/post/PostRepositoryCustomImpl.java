package com.kosa.kmt.nonController.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    
    private final EntityManager em;

//    @Override
//    public List<Post> findAllOrderByBookmarksDesc() {
//        String jpql = "SELECT p FROM Post p LEFT JOIN p.bookMarks b GROUP BY p ORDER BY COUNT(b) DESC";
//        TypedQuery<Post> query = em.createQuery(jpql, Post.class);
//        return query.getResultList();
//    }
//
//    @Override
//    public List<Post> findAllOrderByCommentsDesc() {
//        String jpql = "SELECT p FROM Post p LEFT JOIN p.comments c GROUP BY p ORDER BY COUNT(c) DESC";
//        TypedQuery<Post> query = em.createQuery(jpql, Post.class);
//        return query.getResultList();
//    }

    @Override
    public List<Post> findPostsWithMoreHatesThanLikes() {
        String jpql = "SELECT p FROM Post p " +
                "WHERE (SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p) < " +
                "(SELECT COUNT(ph) FROM PostHate ph WHERE ph.post = p)";

        TypedQuery<Post> query = em.createQuery(jpql, Post.class);
        return query.getResultList();
    }

    @Override
    public List<Post> findPostsByAnyHashtags(Long boardId, List<String> hashtags, long size) {
        // Step 1: 해시태그 이름을 기반으로 해시태그 ID 목록을 조회합니다.
        TypedQuery<Long> hashtagQuery = em.createQuery(
                "SELECT h.id FROM Hashtag h WHERE h.name IN :hashtags", Long.class);
        hashtagQuery.setParameter("hashtags", hashtags);
        List<Long> hashtagIds = hashtagQuery.getResultList();

        // Step 2: 게시물 ID를 조회합니다.
        TypedQuery<Long> postIdQuery = em.createQuery(
                "SELECT DISTINCT p.id " +
                        "FROM Post p " +
                        "JOIN p.hashtags ph " +
                        "JOIN ph.hashtag h " +
                        "JOIN p.category c " +
                        "JOIN c.board b " +
                        "WHERE b.id = :boardId " +
                        "AND h.id IN :hashtagIds", Long.class);
        postIdQuery.setParameter("boardId", boardId);
        postIdQuery.setParameter("hashtagIds", hashtagIds);
        List<Long> postIds = postIdQuery.getResultList();

        // Step 3: 게시물 데이터를 조회합니다.
        TypedQuery<Post> postQuery = em.createQuery(
                "SELECT p " +
                        "FROM Post p " +
                        "WHERE p.id IN :postIds", Post.class);
        postQuery.setParameter("postIds", postIds);

        return postQuery.getResultList();
    }
}
