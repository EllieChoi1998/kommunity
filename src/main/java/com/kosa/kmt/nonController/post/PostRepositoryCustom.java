package com.kosa.kmt.nonController.post;

import java.util.List;

public interface PostRepositoryCustom {
//    List<Post> findAllOrderByBookmarksDesc();
//
//    List<Post> findAllOrderByCommentsDesc();

    List<Post> findPostsWithMoreHatesThanLikes();
}