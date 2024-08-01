package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.member.Member;

public interface PostLikeOrHateService {

    public void likePost(Post post, Member member);

    public void hatePost(Post post, Member member);

    boolean isPostLikedByMember(Post post, Member member);

    boolean isPostHateByMember(Post post, Member member);
}
