package com.kosa.kmt.nonController.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    CommentLike findByPostComment_CommentIdAndMember_MemberId(Long commentId, Integer memberId);
}