package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.member.signup.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("member", new Member());
        model.addAttribute("members", members);
        return "/admin/register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberDTO memberDTO) {
        try {
            if (memberService.isEmailExists(memberDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
            }

            // 유효성 검사
            if (memberDTO.getName() == null || memberDTO.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
            }

            Member member = Member.builder()
                    .name(memberDTO.getName())
                    .email(memberDTO.getEmail())
                    .build();

            memberService.registerMember(member);
            return ResponseEntity.ok("회원 정보가 추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 등록 중 오류가 발생했습니다.");
        }
    }

    // 회원 삭제
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMember(@PathVariable Integer id) {
        try {
            memberService.deleteMemberById(id);
            return ResponseEntity.ok("회원이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 삭제 중 오류가 발생했습니다.");
        }
    }
}
