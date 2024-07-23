package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import org.springframework.stereotype.Service;

public interface PostHashtagService {
    public void setHashtag(Post post, String strHashtag);
}
