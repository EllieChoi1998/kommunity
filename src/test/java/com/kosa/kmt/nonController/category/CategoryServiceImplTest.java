package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardRepository;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryRepository;
import com.kosa.kmt.nonController.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    private Board board;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    public void setUp() {
        this.category = new Category();
        this.category.setName("testCategory");
        this.board = new Board();
        this.board.setName("testBoard");

        boardRepository.saveBoard(board);
    }

    /*
    아무것도 없는 데이터베이스에 새로운 카테고리 추가하는 개념의 성공 케이스
     */
    @Test
    public void testCreateCategory_valid_1() {
        Long result = this.categoryService.addNewCategory(this.category, this.board);
        Long expected = this.category.getCategoryId();

        assertEquals(expected, result);
    }

    /*
    카테고리 데이터베이스 안에는 같은 이름의 카테고리가 존재하지만, 이를 다른 보드에 저장할때의 성공 케이스
     */
    @Test
    public void testCreateCategory_valid_2() {
        Board anotherBoard = new Board();
        anotherBoard.setName("anotherBoard");
        boardRepository.saveBoard(anotherBoard);

        Category anotherCategory = new Category();
        anotherCategory.setName("testCategory");

        Long result = this.categoryService.addNewCategory(anotherCategory, anotherBoard);
        Long expected = anotherCategory.getCategoryId();
        assertEquals(expected, result);
    }

    @Test
    public void testCreateCategory_invalid() {
        Category invalidcategory = new Category();
        invalidcategory.setName("testCategory");
        this.categoryService.addNewCategory(this.category, this.board);
        Long result = this.categoryService.addNewCategory(invalidcategory, this.board);
        Long expected = (long) -1;

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteCategory_valid() {
        Long expected = this.categoryService.addNewCategory(this.category, this.board);
        Long result = this.categoryService.deleteCategory(this.category, this.board);
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteCategory_invalid() {
        Category invalidcategory = new Category();
        invalidcategory.setName("invalidCategory");
        this.categoryService.addNewCategory(this.category, this.board);
        Long result = this.categoryService.deleteCategory(invalidcategory, this.board);
        Long expected = (long) -1;

        assertEquals(expected, result);
    }

    @Test
    public void testFindCategory_valid() {
        this.categoryService.addNewCategory(this.category, this.board);
        Category anothercategory = new Category();
        anothercategory.setName("anothercategory");
        this.categoryService.addNewCategory(anothercategory, this.board);
        Integer expected = 2;
        Integer result = this.categoryService.findAllCategories().size();
        assertEquals(expected, result);

    }

    @Test
    public void testFindCategoryByBoard(){
        this.categoryService.addNewCategory(this.category, this.board);
        Category anothercategory = new Category();
        anothercategory.setName("anothercategory");
        this.categoryService.addNewCategory(anothercategory, this.board);
        Board anotherboard = new Board();
        anotherboard.setName("anotherboard");
        boardRepository.saveBoard(anotherboard);
        this.categoryService.addNewCategory(anothercategory, anotherboard);
        Integer expected = 1;
        Integer result = this.categoryService.findCategoriesByBoard(anotherboard).size();
        assertEquals(expected, result);
    }



}
