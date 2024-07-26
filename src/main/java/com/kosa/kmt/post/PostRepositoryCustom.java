package com.kosa.kmt.post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllOrderByBookmarksDesc();
    List<Post> findAllOrderByCommentsDesc();
}
