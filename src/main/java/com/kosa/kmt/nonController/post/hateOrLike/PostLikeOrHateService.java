package com.kosa.kmt.nonController.post.hateOrLike;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;

public interface PostLikeOrHateService {

    public void likePost(Post post, Member member);

    public void hatePost(Post post, Member member);

}
