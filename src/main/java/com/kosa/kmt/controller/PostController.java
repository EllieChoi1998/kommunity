package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.hashtag.*;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostRepository;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
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

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

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

//    @GetMapping("/new/{boardId}")
//    public String showCreatePostForm(@PathVariable Long boardId, @RequestParam(required = false) Long categoryId, Model model) {
//        List<Category> categories = categoryService.findCategoriesByBoardId(boardId);
//        PostForm postForm = new PostForm();
//        if (categoryId != null) {
//            postForm.setCategoryId(Math.toIntExact(categoryId)); // 카테고리 ID 설정
//        }
//
//        model.addAttribute("categories", categories);
//        model.addAttribute("postForm", postForm);
//        model.addAttribute("boardId", boardId);
//        model.addAttribute("categoryId", categoryId);
//        return "posts/create";
//    }

    @GetMapping("/new/{boardId}")
    public String showCreatePostForm(@PathVariable Long boardId, @RequestParam(required = false) Long categoryId, Model model) throws SQLException {
        List<Category> categories = categoryService.findCategoriesByBoardId(boardId);
        PostForm postForm = new PostForm();
        if (categoryId != null) {
            postForm.setCategoryId(Math.toIntExact(categoryId)); // 카테고리 ID 설정
        }

        addCommonAttributes(model, boardId);

        model.addAttribute("postForm", postForm);
        model.addAttribute("boardId", boardId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("leftSidebar", false);
        model.addAttribute("rightSidebar", false);
        return "posts/create";
    }

    @PostMapping("/new")
    public String createPost(@ModelAttribute PostForm postForm, @RequestParam Long boardId, @RequestParam Long categoryId) throws SQLException {
        Member member = mainController.getCurrentMember();
        postService.createPost(postForm.getTitle(), postForm.getContent(), member.getMemberId(), Math.toIntExact(categoryId), postForm.getStrHashtag());
        return "redirect:/posts/category/" + boardId + "/" + categoryId;
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
