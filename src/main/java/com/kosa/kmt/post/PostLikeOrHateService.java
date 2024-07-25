package com.kosa.kmt.post;

import com.kosa.kmt.member.Member;

public interface PostLikeOrHateService {

    public void likePost(Post post, Member member);

    public void hatePost(Post post, Member member);

}
