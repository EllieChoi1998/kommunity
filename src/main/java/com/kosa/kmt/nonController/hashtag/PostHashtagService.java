package com.kosa.kmt.nonController.hashtag;

import com.kosa.kmt.nonController.post.Post;

import java.util.List;

public interface PostHashtagService {
    public void setHashtag(Post post, String strHashtag);

    List<PostHashtag> getPostHashtags(Post post);
}
