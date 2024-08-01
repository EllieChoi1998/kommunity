package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Long addNewCategory(Category category, Board board);

    Long deleteCategory(Category category, Board board);

    List<Category> findCategoriesByBoard(Board board);

    List<Category> findAllCategories();

    List<Category> findCategoriesByBoardId(Long boardId);

    Category findById(Long categoryId);

    Optional<Category> findCategoryByIdAndBoard(Long categoryId, Board board);
}
