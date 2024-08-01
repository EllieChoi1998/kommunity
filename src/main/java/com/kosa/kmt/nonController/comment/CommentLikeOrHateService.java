package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.member.Member;
import org.springframework.stereotype.Service;

@Service
public interface CommentLikeOrHateService {

    void likePostComment(PostComment comment, Member member);

    void hatePostComment(PostComment comment, Member member);

    boolean isCommentLikedByMember(PostComment comment, Member member);

    boolean isCommentHatedByMember(PostComment comment, Member member);
}