package com.kosa.kmt.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPostDateDesc();
    List<Post> findAllByOrderByPostDateAsc();
}
