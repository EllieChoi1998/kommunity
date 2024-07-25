//package com.kosa.kmt.comment;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
//    long countByPostCommentId(Long commentId);
//    void deleteByPostCommentIdAndMemberId(Long commentId, Long memberId);
//    boolean existsByPostCommentIdAndMemberId(Long commentId, Long memberId);
//}
