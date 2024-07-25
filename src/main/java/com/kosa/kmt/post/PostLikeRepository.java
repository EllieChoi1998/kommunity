package com.kosa.kmt.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByPost_IdAndMember_MemberId(Long id, Integer memberId);
}
