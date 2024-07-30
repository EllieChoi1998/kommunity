package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PostService postService;

    private final MainController mainController;

    @GetMapping("/danamusup")
    public String danamusup() {
        return "category-danamusup"; // category-danamusup.html 페이지를 반환합니다.
    }

    @GetMapping("/codingtest")
    public String codingtest() {
        return "category-codingtest"; // category-codingtest.html 페이지를 반환합니다.
    }

    @GetMapping("/jobtips")
    public String jobtips() {
        return "category-jobtips"; // category-jobtips.html 페이지를 반환합니다.
    }

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

//    @GetMapping("/{boardId}/{categoryId}")
//    public String getPostsByCategory(@PathVariable Long boardId, @PathVariable Long categoryId, Model model) throws SQLException {
//        Optional<Board> optionalBoard = Optional.ofNullable(boardService.findBoardById(boardId));
//        if (!optionalBoard.isPresent()) {
//            return "error/404";
//        }
//
//        Board board = optionalBoard.get();
//        Optional<Category> optionalCategory = categoryService.findCategoryByIdAndBoard(categoryId, board);
//        if (!optionalCategory.isPresent()) {
//            return "error/404";
//        }
//
//        Category category = optionalCategory.get();
//        List<Post> posts = postService.getPostsByCategory(categoryId);
//
//        List<Board> boards = boardService.findAllBoards();
//        Map<Long, List<Category>> boardCategories = boards.stream()
//                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));
//
//        List<Category> categories = categoryService.findCategoriesByBoard(board);
//
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardCategories", boardCategories);
//
//        model.addAttribute("board", board);
//        model.addAttribute("categories", categories);
//        model.addAttribute("category", category);
//        model.addAttribute("posts", posts);
//        model.addAttribute("isAllCategories", false);
//        return "posts/categoryPosts";
//    }

}
