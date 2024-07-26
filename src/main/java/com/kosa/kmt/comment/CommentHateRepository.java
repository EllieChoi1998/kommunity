package com.kosa.kmt.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentHateRepository extends JpaRepository<CommentHate, Long> {
    CommentHate findByPostComment_CommentIdAndMember_MemberId(Long commentId, Integer memberId);
}