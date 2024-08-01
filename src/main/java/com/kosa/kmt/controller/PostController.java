package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;

import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;

import com.kosa.kmt.nonController.hashtag.*;
import com.kosa.kmt.nonController.markdown.MarkdownService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.*;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    private final MarkdownService markdownService;

    public static List<HashtagDTO> staticHashtagDTOs;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostHateRepository postHateRepository;
    @Autowired
    private PostLikeOrHateService postLikeOrHateService;


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
        String renderedContent = markdownService.renderMarkdownToHtml(post.getContent());

        Long boardId = post.getCategory().getBoard().getBoardId();
        addCommonAttributes(model, boardId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("member", member);
        model.addAttribute("renderedContent", renderedContent);
        model.addAttribute("commentForm", new CommentForm()); // 추가된 부분
        return "posts/detail";
    }

    @GetMapping("/new/{boardId}")
    public String showCreatePostForm(@PathVariable Long boardId, @RequestParam(required = false) Long categoryId, Model model) throws SQLException {
        List<Category> categories = categoryService.findCategoriesByBoardId(boardId);
        PostForm postForm = new PostForm();
        postForm.setBoardId(Math.toIntExact(boardId));
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
    public String createPost(@ModelAttribute PostForm postForm) throws SQLException {
        Member member = mainController.getCurrentMember();
        String renderedContent = markdownService.renderMarkdownToHtml(postForm.getContent());
        postService.createPost(postForm.getTitle(), renderedContent, member.getMemberId(), postForm.getCategoryId(), postForm.getStrHashtag());
        return "redirect:/posts/category/" + postForm.getBoardId() + "/" + postForm.getCategoryId();
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

    private List<HashtagDTO> findAllHastags_by_boardId(List<Category> categories, Board board) throws SQLException {
        Map<Long, Post> posts = new HashMap<>();
        for (Category category : categories) {
            List<Post> tmp = postRepository.findByCategoryCategoryId(category.getCategoryId());
            for (Post post : tmp) {
                if (!posts.containsKey(post.getId())) {
                    posts.put(post.getId(), post);
                }
            }
        }

        Map<Long, HashtagDTO> foundHashtagDTOS = new HashMap<>();
        for (Long postId : posts.keySet()) {
            Post post = posts.get(postId);
            List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost_Id(postId);
            if (postHashtags.size() > 0) {
                for (PostHashtag postHashtag : postHashtags) {
                    Long hashtagId = postHashtag.getHashtag().getId();
                    Optional<Hashtag> optionalHashtag = hashtagRepository.findById(hashtagId);
                    if (optionalHashtag.isPresent()) {
                        String hashtagName = optionalHashtag.get().getName();
                        if (foundHashtagDTOS.containsKey(hashtagId)) {
                            HashtagDTO tmp = foundHashtagDTOS.get(hashtagId);
                            tmp.addPosts(post);
                            foundHashtagDTOS.replace(hashtagId, tmp);
                        } else {
                            HashtagDTO hashtagDTO = new HashtagDTO(hashtagName, hashtagId);
                            foundHashtagDTOS.put(hashtagId, hashtagDTO);
                            hashtagDTO.addPosts(post);
                        }

                    } else {
                        // hashtagId에 해당하는 Hashtag가 없는 경우 처리
                        System.out.println("Hashtag not found for id: " + hashtagId);
                    }
                }
            }
        }

        List<HashtagDTO> hashtagDTOS = new ArrayList<>();
        for (Long hashtagId : foundHashtagDTOS.keySet()) {
            hashtagDTOS.add(foundHashtagDTOS.get(hashtagId));
        }

        staticHashtagDTOs = hashtagDTOS;

        return hashtagDTOS;

    }

    private void addCommonAttributes(Model model, Long boardId) throws SQLException {
        List<Board> boards = boardService.findAllBoards();
        Board board = boardId != null ? boardService.findBoardById(boardId).orElse(null) : null;
        List<Category> categories = boardId != null ? categoryService.findCategoriesByBoardId(boardId) : List.of();

        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));

        List<HashtagDTO> hashtagDTO = findAllHastags_by_boardId(categories, board);

        List<HashtagDTO> sortedHashtagDTO = hashtagDTO.stream()
                .sorted(Comparator.comparingInt(HashtagDTO::getCount).reversed())
                .collect(Collectors.toList());

        model.addAttribute("member", mainController.getCurrentMember());
        model.addAttribute("boards", boards);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("selectedBoardId", boardId);
        model.addAttribute("sortedHashtagDTO", sortedHashtagDTO);
    }

    private List<PostForm> convertToPostForms(List<Post> posts) {
        return posts.stream().map(post -> {
            PostForm postForm = new PostForm();
            postForm.setId(post.getId());
            postForm.setTitle(post.getTitle());
            postForm.setContent(post.getContent());
            postForm.setRenderedContent(MarkdownService.renderMarkdownToHtml(post.getContent()));
            postForm.setMemberId(post.getMember().getMemberId());
            postForm.setNickname(post.getMember().getNickname());
            if (post.getCategory() != null) {
                postForm.setCategoryId(Math.toIntExact(post.getCategory().getCategoryId()));
                postForm.setBoardId(Math.toIntExact(post.getCategory().getBoard().getBoardId()));
            } else {
                postForm.setCategoryId(null);
                postForm.setBoardId(null);
            }
            postForm.setPostDate(post.getPostDate());
            postForm.setHashtags(postHashtagRepository.findAllByPost_Id(post.getId()));
            return postForm;
        }).collect(Collectors.toList());
    }


    @GetMapping("/board/{boardId}")
    public String getPostsByBoard(@PathVariable Long boardId,
                                  @RequestParam(defaultValue = "desc") String sort,
                                  Model model) throws SQLException {
        Optional<Board> optionalBoard = boardService.findBoardById(boardId);
        if (optionalBoard.isEmpty()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        List<Post> posts;
        switch (sort) {
            case "asc":
                posts = postService.getPostsByBoardOrderByPostDateAsc(boardId);
                break;
            case "bookmarks":
                posts = postService.getPostsByBoardOrderByBookmarksDesc(boardId);
                break;
            case "comments":
                posts = postService.getPostsByBoardOrderByCommentsDesc(boardId);
                break;
            case "desc":
            default:
                posts = postService.getPostsByBoardOrderByPostDateDesc(boardId);
                break;
        }
        List<PostForm> postForms = convertToPostForms(posts);

        addCommonAttributes(model, boardId);
        model.addAttribute("posts", postForms);
        model.addAttribute("isAllCategories", true);
        model.addAttribute("selectedSort", sort);
        return "posts/posts";
    }

    @GetMapping("/category/{boardId}/{categoryId}")
    public String getPostsByCategory(@PathVariable Long boardId, @PathVariable Long categoryId,
                                     @RequestParam(defaultValue = "desc") String sort,
                                     Model model) throws SQLException {
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
        List<Post> posts;
        switch (sort) {
            case "asc":
                posts = postService.getPostsByCategoryOrderByPostDateAsc(categoryId);
                break;
            case "bookmarks":
                posts = postService.getPostsByCategoryOrderByBookmarksDesc(categoryId);
                break;
            case "comments":
                posts = postService.getPostsByCategoryOrderByCommentsDesc(categoryId);
                break;
            case "desc":
            default:
                posts = postService.getPostsByCategoryOrderByPostDateDesc(categoryId);
                break;
        }
        List<PostForm> postForms = convertToPostForms(posts);

        addCommonAttributes(model, boardId);
        model.addAttribute("category", category);
        model.addAttribute("posts", postForms);
        model.addAttribute("isAllCategories", false);
        model.addAttribute("selectedSort", sort);
        return "posts/posts";
    }

    // 좋아요 기능
    @PostMapping("/like/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) throws SQLException {
        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();
        postLikeOrHateService.likePost(post, member);
        return ResponseEntity.ok().build();
    }

    // 싫어요 기능
    @PostMapping("/dislike/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> dislikePost(@PathVariable Long postId) throws SQLException {
        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();
        postLikeOrHateService.hatePost(post, member);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/{boardId}")
    public String searchPosts(@PathVariable Long boardId,
                              @RequestParam List<String> hashtags,
                              Model model) throws SQLException {
        List<Post> posts = postService.findPostsByAnyHashtags(boardId, hashtags);

        List<PostForm> postForms = convertToPostForms(posts);

        addCommonAttributes(model, boardId);
        model.addAttribute("posts", postForms);
        model.addAttribute("hashtags", hashtags);
        return "posts/searchPosts";
    }
}
