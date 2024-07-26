package com.kosa.kmt.non_controller.comment;

import com.kosa.kmt.non_controller.member.Member;
import org.springframework.stereotype.Service;

@Service
public interface CommentLikeOrHateService {

    void likePostComment(PostComment comment, Member member);

    void hatePostComment(PostComment comment, Member member);
}