package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
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

    private final CommentService commentService;

    private final MainController mainController;

    @GetMapping
    public String getAllPosts(Model model) throws SQLException {
        List<Post> posts = postService.getPostsAll();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) throws SQLException {
        Post post = postService.getPostById(id);
        Member member = mainController.getCurrentMember(); // 현재 사용자를 가져오는 로직
        List<PostComment> comments = commentService.getCommentsByPostId(id); // Comment 서비스가 있다고 가정합니다.

        addCommonAttributes(model, post.getCategory().getBoard().getBoardId());

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("member", member);
        model.addAttribute("commentForm", new CommentForm()); // 추가된 부분
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
        Board board = boardId != null ? boardService.findBoardById(boardId).orElse(null) : null;
        List<Category> categories = boardId != null ? categoryService.findCategoriesByBoardId(boardId) : List.of();

        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));

        model.addAttribute("member", mainController.getCurrentMember());
        model.addAttribute("boards", boards);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("boardCategories", boardCategories);
    }

    @GetMapping("/board/{boardId}")
    public String getPostsByBoard(@PathVariable Long boardId, Model model) throws SQLException {
        Optional<Board> optionalBoard = boardService.findBoardById(boardId);
        if (optionalBoard.isEmpty()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        List<Post> posts = postService.getPostsByBoard(boardId);
        addCommonAttributes(model, boardId);

        model.addAttribute("board", board);
        model.addAttribute("posts", posts);
        model.addAttribute("isAllCategories", true);
        return "posts/posts";
    }

    @GetMapping("/category/{boardId}/{categoryId}")
    public String getPostsByCategory(@PathVariable Long boardId, @PathVariable Long categoryId, Model model) throws SQLException {
        Optional<Board> optionalBoard = boardService.findBoardById(boardId);
        if (optionalBoard.isEmpty()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        Optional<Category> optionalCategory = categoryService.findCategoryByIdAndBoard(categoryId, board);
        if (optionalCategory.isEmpty()) {
            return "error/404";
        }

        Category category = optionalCategory.get();
        List<Post> posts = postService.getPostsByCategory(categoryId);

        addCommonAttributes(model, boardId);

        model.addAttribute("category", category);
        model.addAttribute("posts", posts);
        model.addAttribute("isAllCategories", false);
        return "posts/posts";
    }

}
