package com.kosa.kmt.nonController.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, PostCommentRepositoryCustom {
    // 최신 순 정렬
    List<PostComment> findAllByOrderByCommentDateTimeDesc();

    // 오래된 순 정렬
    List<PostComment> findAllByOrderByCommentDateTimeAsc();
}