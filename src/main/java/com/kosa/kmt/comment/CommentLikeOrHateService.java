package com.kosa.kmt.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentLikeOrHateService {

    private final CommentLikeOrHateRepository commentLikeOrHateRepository;

    // 댓글 좋아요 추가
    @Transactional
    public void addLikeToComment(Long commentId, Long memberId) {
        commentLikeOrHateRepository.addLike(commentId, memberId);
    }

    // 댓글 싫어요 추가
    @Transactional
    public void addHateToComment(Long commentId, Long memberId) {
        commentLikeOrHateRepository.addHate(commentId, memberId);
    }

    // 댓글 좋아요 제거
    @Transactional
    public void removeLikeFromComment(Long commentId, Long memberId) {
        commentLikeOrHateRepository.removeLike(commentId, memberId);
    }

    // 댓글 싫어요 제거
    @Transactional
    public void removeHateFromComment(Long commentId, Long memberId) {
        commentLikeOrHateRepository.removeHate(commentId, memberId);
    }

    // 댓글 좋아요 수 카운트
    public long countLikesByCommentId(Long commentId) {
        return commentLikeOrHateRepository.countLikesByCommentId(commentId);
    }

    // 댓글 싫어요 수 카운트
    public long countHatesByCommentId(Long commentId) {
        return commentLikeOrHateRepository.countHatesByCommentId(commentId);
    }
}
