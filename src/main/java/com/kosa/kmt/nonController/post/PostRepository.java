package com.kosa.kmt.nonController.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByOrderByPostDateDesc();

    List<Post> findAllByOrderByPostDateAsc();

    List<Post> findByCategoryCategoryId(Long categoryId);

    List<Post> findAllByCategory_Board_BoardIdOrderByPostDateDesc(Long boardId);

    List<Post> findAllByCategory_Board_BoardIdOrderByPostDateAsc(Long boardId);

    List<Post> findAllByCategory_CategoryIdOrderByPostDateDesc(Long categoryId);

    List<Post> findAllByCategory_CategoryIdOrderByPostDateAsc(Long categoryId);

    List<Post> findAllByCategory_Board_BoardId(Long boardId);

    List<Post> findAllByCategory_CategoryId(Long categoryId);
}
