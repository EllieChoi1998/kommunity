package com.kosa.kmt.post;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface BookMarkRepositoryCustom {

    Long countBookMarksByPost(Post post);

}
