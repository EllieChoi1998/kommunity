package com.kosa.kmt.non_controller.hashtag;

import com.kosa.kmt.non_controller.post.Post;

import java.util.List;

public interface PostHashtagService {
    public void setHashtag(Post post, String strHashtag);

    List<PostHashtag> getPostHashtags(Post post);
}
