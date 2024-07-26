package com.kosa.kmt.non_controller.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Long addNewBoard(Board board) {
        Optional<Board> optionalBoard = boardRepository.findByName(board.getName());
        if (optionalBoard.isPresent()) {
            return (long) -1;
        } else {
            boardRepository.saveBoard(board);
            return board.getBoardId();
        }
    }

    @Override
    public Long deleteBoard(Board board) {
        Optional<Board> optionalBoard = boardRepository.findByName(board.getName());
        if (optionalBoard.isPresent()) {
            boardRepository.deleteBoard(board);
            return board.getBoardId();
        } else {
            return (long) -1;
        }
    }

    @Override
    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }
}
