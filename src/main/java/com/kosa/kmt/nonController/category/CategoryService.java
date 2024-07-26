package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;

import java.util.List;

public interface CategoryService {
    Long addNewCategory(Category category, Board board);
    Long deleteCategory(Category category, Board board);

    List<Category> findCategoriesByBoard(Board board);

    List<Category> findAllCategories();
}
