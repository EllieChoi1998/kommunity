package com.kosa.kmt.non_controller.category;

import com.kosa.kmt.non_controller.board.Board;

import java.util.List;

public interface CategoryService {
    Long addNewCategory(Category category, Board board);
    Long deleteCategory(Category category, Board board);

    List<Category> findCategoriesByBoard(Board board);

    List<Category> findAllCategories();
}
