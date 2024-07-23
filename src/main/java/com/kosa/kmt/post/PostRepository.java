package com.kosa.kmt.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
