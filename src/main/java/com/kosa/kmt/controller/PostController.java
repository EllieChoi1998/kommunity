package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final BoardService boardService;

    private final CategoryService categoryService;

    @GetMapping
    public String getAllPosts(Model model) throws SQLException {
        List<Post> posts = postService.getPostsAll();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) throws SQLException {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/new")
    public String createPostForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "posts/create";
    }

    @PostMapping
    public String createPost(@ModelAttribute PostForm postForm) throws SQLException {
        postService.createPost(postForm.getTitle(), postForm.getContent(), postForm.getMemberId(), postForm.getCategoryId(), postForm.getStrHashtag());
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) throws SQLException {
        Post post = postService.getPostById(id);
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute PostForm postForm) throws SQLException {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        postService.updatePost(post, id);
        return "redirect:/posts";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) throws SQLException {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    private void addCommonAttributes(Model model, Long boardId) throws SQLException {
        List<Board> boards = boardService.findAllBoards();
        Board board = boardService.findBoardById(boardId);
        List<Category> categories = categoryService.findCategoriesByBoardId(boardId);

        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));

        model.addAttribute("boards", boards);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("boardCategories", boardCategories);
    }

    @GetMapping("/board/{boardId}")
    public String getPostsByBoard(@PathVariable Long boardId, Model model) throws SQLException {
        Optional<Board> optionalBoard = Optional.ofNullable(boardService.findBoardById(boardId));

        if (!optionalBoard.isPresent()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        List<Post> posts = postService.getPostsByBoard(boardId);

        addCommonAttributes(model, boardId);

        model.addAttribute("board", board);
        model.addAttribute("posts", posts);
        model.addAttribute("isAllCategories", true);
        return "posts/boardPosts";
    }

    @GetMapping("/board/{boardId}/category/{categoryId}")
    public String getPostsByCategory(@PathVariable Long boardId, @PathVariable Long categoryId, Model model) throws SQLException {
        Category category = categoryService.findCategoryById(categoryId);
        List<Post> posts = postService.getPostsByCategory(categoryId);
        addCommonAttributes(model, boardId);
        model.addAttribute("category", category);
        model.addAttribute("posts", posts);
        model.addAttribute("isAllCategories", false);
        return "posts/categoryPosts";
    }
}
