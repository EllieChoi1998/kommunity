package com.kosa.kmt.post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByOrderByPostDateDesc();

    List<Post> findAllByOrderByPostDateAsc();
}
