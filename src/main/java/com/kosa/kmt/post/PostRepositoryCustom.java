package com.kosa.kmt.post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {

    //    public LocalDateTime findPostDateById(Long id);

    List<Post> findAllByOrderByPostDateDesc();

    List<Post> findAllByOrderByPostDateAsc();
}
