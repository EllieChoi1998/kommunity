package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class CategoryEntityTest {
    private Category category;
    private Board board;
    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Entity Test");

        board = new Board();
        board.setName("Category Entity Test");
        category.setBoard(board);
    }

    @Test
    public void testGetBoardId_valid(){
        Long expected = board.getBoardId();
        Long actual = category.getBoard().getBoardId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBoardId_invalid(){
        Long expected = 0L;
        Long actual = category.getBoard().getBoardId();
        assertNotEquals(expected, actual);
    }
}
