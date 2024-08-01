package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        List<Board> boards = boardService.findAllBoards();
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categories);
        return "admin/dashboard";
    }

    // 보드 추가
    @PostMapping("/boards/add")
    public String addBoard(@RequestParam String boardName) {
        Board board = new Board();
        board.setName(boardName);
        boardService.addNewBoard(board);
        return "redirect:/admin/dashboard";
    }

    // 보드 삭제
    @DeleteMapping("/boards/delete/{boardId}")
    public String deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoardWithCategories(boardId);
        return "redirect:/admin/dashboard";
    }

    // 카테고리 추가
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String categoryName, @RequestParam Long boardId) {
        Category category = new Category();
        category.setName(categoryName);
        Optional<Board> board = boardService.findBoardById(boardId);
        if (board.isEmpty()) {
            return "error/404";
        }

        category.setBoard(board.get());
        categoryService.addNewCategory(category, board.get());

        return "redirect:/admin/dashboard";
    }

    // 카테고리 삭제
    @DeleteMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new Member());
        return "/admin/register";
    }

    @PostMapping("/register")
    public String registerMember(@RequestParam String name, @RequestParam String email, Model model) {
        try {
            Member member = Member.builder()
                    .name(name)
                    .email(email)
                    .build();

            memberService.registerMember(member);

            model.addAttribute("successMessage", "회원 가입이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원 가입에 실패하였습니다. 다시 시도해주세요.");
        }
        return "redirect:/admin/register";
    }
}
