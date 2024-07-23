package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long>, PostHashtagRepositoryCustom {
    Optional<PostHashtag> findByPost_IdAndHashtag_Id(Long postId, Long hashtagId);
}
