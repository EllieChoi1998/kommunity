package com.kosa.kmt.nonController.board;

import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryRepository;
import com.kosa.kmt.nonController.category.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    public BoardServiceImpl(BoardRepository boardRepository, CategoryRepository categoryRepository) {
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
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

    @Override
    public Optional<Board> findBoardById(Long boardId) {
        return boardRepository.findBoardById(boardId);
    }

    @Override
    @Transactional
    public void deleteBoardWithCategories(Long boardId) {
        Optional<Board> board = boardRepository.findBoardById(boardId);
        if (board.isPresent()) {
            // 하위 카테고리 삭제
            List<Category> categories = board.get().getCategories();
            for (Category category : categories) {
                categoryRepository.deleteCategory(category);
            }
            // 보드 삭제
            boardRepository.deleteBoard(board.get());
        }
    }
}
