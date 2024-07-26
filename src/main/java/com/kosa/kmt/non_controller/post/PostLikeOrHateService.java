package com.kosa.kmt.non_controller.post;

import com.kosa.kmt.non_controller.member.Member;

public interface PostLikeOrHateService {

    public void likePost(Post post, Member member);

    public void hatePost(Post post, Member member);

}
