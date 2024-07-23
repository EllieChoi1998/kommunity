package com.kosa.kmt.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentLikeOrHateService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentHateRepository commentHateRepository;

    @Transactional
    public void addLikeToComment(Long commentId, Long memberId) {
        if (commentHateRepository.existsByPostCommentIdAndMemberId(commentId, memberId)) {
            removeHateFromComment(commentId, memberId);
        }
        CommentLike like = new CommentLike();
        like.setPostComment(new PostComment());
        like.getPostComment().setComment_Id(commentId);
        like.setMemberId(memberId);
        commentLikeRepository.save(like);
    }

    @Transactional
    public void addHateToComment(Long commentId, Long memberId) {
        if (commentLikeRepository.existsByPostCommentIdAndMemberId(commentId, memberId)) {
            removeLikeFromComment(commentId, memberId);
        }
        CommentHate hate = new CommentHate();
        hate.setPostComment(new PostComment());
        hate.getPostComment().setComment_Id(commentId);
        hate.setMemberId(memberId);
        commentHateRepository.save(hate);
    }

    @Transactional
    public void removeLikeFromComment(Long commentId, Long memberId) {
        commentLikeRepository.deleteByPostCommentIdAndMemberId(commentId, memberId);
    }

    @Transactional
    public void removeHateFromComment(Long commentId, Long memberId) {
        commentHateRepository.deleteByPostCommentIdAndMemberId(commentId, memberId);
    }

    public long countLikesByCommentId(Long commentId) {
        return commentLikeRepository.countByPostCommentId(commentId);
    }

    public long countHatesByCommentId(Long commentId) {
        return commentHateRepository.countByPostCommentId(commentId);
    }
}
