package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category saveCategory(Category category);
    Category deleteCategory(Category category);
    Optional<Category> findByName(String name, Long boardId);
    List<Category> findAll();
    List<Category> findByBoard(Board board);
    List<Category> findByBoardId(Long boardId);
    Optional<Category> findById(Long categoryId);
    Optional<Category> findByIdAndBoard(Long id, Board board);
}
