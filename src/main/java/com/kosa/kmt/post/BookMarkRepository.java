package com.kosa.kmt.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long>, BookMarkRepositoryCustom {

    Optional<BookMark> findByPost_IdAndMember_MemberId(Long postId, Integer memberId);

    List<BookMark> findAllByMember_MemberId(Integer memberId);

    List<BookMark> findAllByPost_Id(Long id);

    void deleteByPost_IdAndMember_MemberId(Long postId, Integer memberId);
}
