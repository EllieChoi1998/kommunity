package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long>, PostHashtagRepositoryCustom {
    void findByPostAndHashtag(Long postId, Long hashtagId);
}
