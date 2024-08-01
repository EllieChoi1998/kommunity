package com.kosa.kmt.nonController.board;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    Long addNewBoard(Board board);
    Long deleteBoard(Board board);
    List<Board> findAllBoards();

    Optional<Board> findBoardById(Long boardId);
    void deleteBoardWithCategories(Long boardId);
}
