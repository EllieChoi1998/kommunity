package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.post.Post;

public interface PostCommentRepositoryCustom {

    public Long countCommentsByPost(Post post);
}
