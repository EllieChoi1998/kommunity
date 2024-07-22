package com.kosa.kmt.board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board saveBoard(Board board);
    Board deleteBoard(Board board);
    Optional<Board> findByName(String name);
    List<Board> findAll();
}