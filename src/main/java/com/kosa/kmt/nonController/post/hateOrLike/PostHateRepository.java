package com.kosa.kmt.nonController.post.hateOrLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHateRepository extends JpaRepository<PostHate, Long> {
    PostHate findByPost_IdAndMember_MemberId(Long id, Integer memberId);
}
