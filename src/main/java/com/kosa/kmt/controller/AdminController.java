package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("boards", boardService.findAllBoards());
        model.addAttribute("categories", categoryService.findAllCategories());
        return "admin/dashboard";
    }

    @PostMapping("/boards/add")
    public String addBoard(@ModelAttribute Board board) {
        boardService.addNewBoard(board);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/boards/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.deleteBoardById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, Board board) {
        categoryService.addNewCategory(category, board);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/dashboard";
    }
}
