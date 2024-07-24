package com.kosa.kmt.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long>, PostHashtagRepositoryCustom {
    Optional<PostHashtag> findByPost_IdAndHashtag_Id(Long postId, Long hashtagId);

    List<PostHashtag> findAllByPost_Id(Long postId);
}
