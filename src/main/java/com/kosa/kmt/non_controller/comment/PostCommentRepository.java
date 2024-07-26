package com.kosa.kmt.non_controller.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    // 최신 순 정렬
    List<PostComment> findAllByOrderByCommentDateTimeDesc();

    // 오래된 순 정렬
    List<PostComment> findAllByOrderByCommentDateTimeAsc();
}
