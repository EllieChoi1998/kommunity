package com.kosa.kmt.non_controller.comment;

import com.kosa.kmt.non_controller.post.Post;

public interface PostCommentRepositoryCustom {

    public Long countCommentsByPost(Post post);
}
