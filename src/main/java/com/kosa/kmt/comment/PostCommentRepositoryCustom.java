package com.kosa.kmt.comment;

import com.kosa.kmt.post.Post;

public interface PostCommentRepositoryCustom {

    public Long countCommentsByPost(Post post);
}
