package com.kosa.kmt.non_controller.comment;

import com.kosa.kmt.non_controller.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeOrHateServiceImpl implements CommentLikeOrHateService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentHateRepository commentHateRepository;

    @Override
    public void likePostComment(PostComment comment, Member member) {
        CommentLike existingLike = commentLikeRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId());
        if (existingLike != null) {
            commentLikeRepository.delete(existingLike);
        } else {
            CommentHate existingHate = commentHateRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId());
            if (existingHate != null) {
                commentHateRepository.delete(existingHate);
            }
            CommentLike commentLike = new CommentLike();
            commentLike.setPostComment(comment);
            commentLike.setMember(member);
            commentLikeRepository.save(commentLike);
        }
    }

    @Override
    public void hatePostComment(PostComment comment, Member member) {
        CommentHate existingHate = commentHateRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId());
        if (existingHate != null) {
            commentHateRepository.delete(existingHate);
        } else {
            CommentLike existingLike = commentLikeRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId());
            if (existingLike != null) {
                commentLikeRepository.delete(existingLike);
            }
            CommentHate commentHate = new CommentHate();
            commentHate.setPostComment(comment);
            commentHate.setMember(member);
            commentHateRepository.save(commentHate);
        }
    }
}
