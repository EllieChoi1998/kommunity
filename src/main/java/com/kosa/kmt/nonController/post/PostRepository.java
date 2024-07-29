package com.kosa.kmt.nonController.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByPostDateDesc();
    List<Post> findAllByOrderByPostDateAsc();
}
