package com.kosa.kmt.comment;

import com.kosa.kmt.member.Member;
import org.springframework.stereotype.Service;

@Service
public interface CommentLikeOrHateService {

    void likePostComment(PostComment comment, Member member);

    void hatePostComment(PostComment comment, Member member);
}