package com.kosa.kmt.nonController.post.bookmark;

import com.kosa.kmt.nonController.post.Post;

public interface BookMarkRepositoryCustom {

    Long countBookMarksByPost(Post post);

}
