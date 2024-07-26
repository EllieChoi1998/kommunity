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
}
