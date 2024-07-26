package com.kosa.kmt.non_controller.post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllOrderByBookmarksDesc();
    List<Post> findAllOrderByCommentsDesc();
}
