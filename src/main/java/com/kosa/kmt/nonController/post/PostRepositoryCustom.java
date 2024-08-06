package com.kosa.kmt.nonController.post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostsWithMoreHatesThanLikes();

    List<Post> findPostsByAnyHashtags(Long boardId, List<String> hashtags, long size);

}
