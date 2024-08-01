package com.kosa.kmt.nonController.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, PostCommentRepositoryCustom {
    // 최신 순 정렬
    List<PostComment> findByPostIdOrderByCommentDateTimeDesc(Long postId);

    // 오래된 순 정렬
    List<PostComment> findByPostIdOrderByCommentDateTimeAsc(Long postId);

    List<PostComment> findByPostId(Long postId);

    // 좋아요 많은 순으로 정렬
    @Query("SELECT c " +
            "FROM PostComment c LEFT JOIN c.likes l " +
            "WHERE c.post.id = :postId " +
            "GROUP BY c " +
            "ORDER BY COUNT(l) DESC")
    List<PostComment> findAllByOrderByLikesDesc(@Param("postId") Long postId);
}
