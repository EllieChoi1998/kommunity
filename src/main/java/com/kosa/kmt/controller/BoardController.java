package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;

    @GetMapping("/boards")
    public String getAllBoards(Model model, HttpSession session) {
        // SecurityContextHolder를 통해 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authentication 객체에서 UserDetails 객체 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // UserDetails 객체에서 역할 정보 가져오기
        String role = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        model.addAttribute("boards", boardService.findAllBoards());
        model.addAttribute("userRole", role); // 사용자 역할 추가

        return "boards/list"; // boards/list.html 템플릿 파일로 이동
    }

    @GetMapping("/boards/new")
    public String showNewBoardForm(Model model) {
        model.addAttribute("board", new Board());
        return "boards/new"; // boards/new.html 템플릿 파일로 이동
    }

    @PostMapping("/boards")
    public String createBoard(@ModelAttribute Board board) {
        boardService.addNewBoard(board);
        return "redirect:/boards";
    }

    @PostMapping("/boards/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.findBoardById(id).ifPresent(boardService::deleteBoard);
        return "redirect:/boards";
    }

    @GetMapping("/categories/new")
    public String showNewCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/new"; // categories/new.html 템플릿 파일로 이동
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute Category category) {
//        categoryService.addNewCategory(category);
        return "redirect:/boards";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
//        categoryService.findCategoryById(id).ifPresent(categoryService::deleteCategory);
        return "redirect:/boards";
    }

}
