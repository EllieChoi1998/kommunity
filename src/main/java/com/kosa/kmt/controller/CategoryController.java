package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/category/danamusup")
    public String danamusup() {
        return "category-danamusup"; // category-danamusup.html 페이지를 반환합니다.
    }

    @GetMapping("/category/codingtest")
    public String codingtest() {
        return "category-codingtest"; // category-codingtest.html 페이지를 반환합니다.
    }

    @GetMapping("/category/jobtips")
    public String jobtips() {
        return "category-jobtips"; // category-jobtips.html 페이지를 반환합니다.
    }

    @GetMapping("/category/{boardName}")
    public String getCategoriesByBoard(@PathVariable String boardName, Model model) {
        Board board = boardService.findAllBoards().stream()
                .filter(b -> b.getName().equals(boardName))
                .findFirst()
                .orElse(null);

        if (board == null) {
            return "error/404"; // or appropriate error page
        }

        List<Category> categories = categoryService.findCategoriesByBoard(board);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        return "category";
    }
}
