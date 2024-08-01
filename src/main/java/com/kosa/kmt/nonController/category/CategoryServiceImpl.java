package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, BoardRepository boardRepository) {
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public Long addNewCategory(Category category, Board board) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName(), board.getBoardId());
        if (optionalCategory.isPresent()) {
            return (long) -1;
        } else {
            category.setBoard(board);
            Category result = categoryRepository.saveCategory(category);
            return result.getCategoryId();
        }
    }

    @Override
    public Long deleteCategory(Category category, Board board) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName(), board.getBoardId());
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteCategory(category);
            return category.getCategoryId();
        } else {
            return (long) -1;
        }
    }

    @Override
    public List<Category> findCategoriesByBoard(Board board) {
        return categoryRepository.findByBoard(board);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findCategoriesByBoardId(Long boardId) {
        return categoryRepository.findByBoardId(boardId);
    }

    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Optional<Category> findCategoryByIdAndBoard(Long categoryId, Board board) {
        return categoryRepository.findByIdAndBoard(categoryId, board);
    }
}
