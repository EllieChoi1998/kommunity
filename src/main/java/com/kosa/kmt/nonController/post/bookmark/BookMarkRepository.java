package com.kosa.kmt.nonController.post.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long>, BookMarkRepositoryCustom {

    Optional<BookMark> findByPost_IdAndMember_MemberId(Long postId, Integer memberId);

    List<BookMark> findAllByMember_MemberId(Integer memberId);

    void deleteByPost_IdAndMember_MemberId(Long postId, Integer memberId);
}
