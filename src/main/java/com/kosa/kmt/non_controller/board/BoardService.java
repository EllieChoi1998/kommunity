package com.kosa.kmt.non_controller.board;

import java.util.List;

public interface BoardService {
    Long addNewBoard(Board board);
    Long deleteBoard(Board board);
    List<Board> findAllBoards();
}
