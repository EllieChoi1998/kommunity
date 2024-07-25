package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostHashtagService {
    public void setHashtag(Post post, String strHashtag);

    List<PostHashtag> getPostHashtags(Post post);
}
