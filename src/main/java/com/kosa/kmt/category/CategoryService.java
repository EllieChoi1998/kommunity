package com.kosa.kmt.category;

import com.kosa.kmt.board.Board;

import java.util.List;

public interface CategoryService {
    Long addNewCategory(Category category, Board board);
    Long deleteCategory(Category category, Board board);

    List<Category> findCategoriesByBoard(Board board);

    List<Category> findAllCategories();
}
