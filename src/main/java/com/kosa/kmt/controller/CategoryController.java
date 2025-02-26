package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    private final MainController mainController;

    @GetMapping("/{boardId}")
    public String getCategoriesByBoard(@PathVariable Long boardId, Model model) throws SQLException {
        Optional<Board> optionalBoard = boardService.findBoardById(boardId);

        if (!optionalBoard.isPresent()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        List<Category> categories = categoryService.findCategoriesByBoard(board);

        List<Board> boards = boardService.findAllBoards();
        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));

        model.addAttribute("boards", boards);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("member", mainController.getCurrentMember());

        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("isAllCategories", true);
        return "category";
    }

}
