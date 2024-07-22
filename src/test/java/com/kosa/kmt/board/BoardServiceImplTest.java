package com.kosa.kmt.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BoardServiceImplTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    private Board board;
    @BeforeEach
    public void setUp() {
        this.board = new Board();
        this.board.setName("testBoard");

    }

    @Test
    public void testCreateBoard_valid() {
        Long result = this.boardService.addNewBoard(this.board);
        Long expected = this.board.getBoardId();

        assertEquals(expected, result);
    }

    @Test
    public void testCreateBoard_invalid() {
        Board invalidboard = new Board();
        invalidboard.setName("testBoard");
        this.boardService.addNewBoard(this.board);
        Long result = this.boardService.addNewBoard(invalidboard);
        Long expected = (long) -1;

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteBoard_valid() {
        Long expected = this.boardService.addNewBoard(this.board);
        Long result = this.boardService.deleteBoard(this.board);
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteBoard_invalid() {
        Board invalidboard = new Board();
        invalidboard.setName("invalidBoard");
        this.boardService.addNewBoard(this.board);
        Long result = this.boardService.deleteBoard(invalidboard);
        Long expected = (long) -1;

        assertEquals(expected, result);
    }

    @Test
    public void testFindBoard_valid() {
        this.boardService.addNewBoard(this.board);
        Board anotherboard = new Board();
        anotherboard.setName("anotherboard");
        this.boardService.addNewBoard(anotherboard);
        Integer expected = 2;
        Integer result = this.boardService.findAllBoards().size();
        assertEquals(expected, result);

    }

}
