package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;


import com.kosa.kmt.nonController.comment.*;

import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;

import com.kosa.kmt.nonController.hashtag.*;
import com.kosa.kmt.nonController.markdown.MarkdownService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private BookMarkRepository bookMarkRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentHateRepository commentHateRepository;

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
        // 댓글을 최신순으로 정렬하여 가져옴
        List<PostComment> comments = commentService.getCommentsByPostIdAndNewest(id);
        String renderedContent = markdownService.renderMarkdownToHtml(post.getContent());
        List<PostHashtag> hashtags = post.getHashtags();

        Long boardId = post.getCategory().getBoard().getBoardId();
        addCommonAttributes(model, boardId);

        // 게시글이 특정 게시판에 속해 있는지 확인
        boolean isAnonymousBoard = boardId == 1 || "대나무숲".equals(post.getCategory().getBoard().getName());

        model.addAttribute("post", post);
        model.addAttribute("member", member);
        model.addAttribute("renderedContent", renderedContent);
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("isAnonymousBoard", isAnonymousBoard); // 익명 표시 여부를 모델에 추가
        model.addAttribute("hashtags", hashtags); // 해시태그를 모델에 추가

        PostDetailsDTO postDetailsDTO = new PostDetailsDTO();
        postDetailsDTO.setPost(post);
        postDetailsDTO.setLikedByCurrentUser(postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()) != null);
        postDetailsDTO.setDislikedByCurrentUser(postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()) != null);
        postDetailsDTO.setBookmarkedByCurrentUser(bookMarkService.isBookMarkedByMember(post, member));

        model.addAttribute("postDetailsDTO", postDetailsDTO);

        List<CommentDetailsDTO> commentDetailsDTOS = new ArrayList<>();
        for (PostComment comment : comments) {
            CommentDetailsDTO commentDetailsDTO = new CommentDetailsDTO();
            commentDetailsDTO.setComment(comment);
            commentDetailsDTO.setLikedByCurrentUser(
                    commentLikeRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId()) != null
            );
            commentDetailsDTO.setDislikedByCurrentUser(
                    commentHateRepository.findByPostComment_CommentIdAndMember_MemberId(comment.getCommentId(), member.getMemberId()) != null
            );
            commentDetailsDTOS.add(commentDetailsDTO);
        }

        model.addAttribute("comments", commentDetailsDTOS);

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

    // 게시물 수정 폼을 보여주는 메서드
    @GetMapping("/{postId}/edit")
    @PreAuthorize("isAuthenticated()")
    public String showUpdateForm(@PathVariable Long postId, Model model) throws SQLException {
        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();
        if (post.getMember().getEmail().equals(member.getEmail())) {
            PostForm postForm = new PostForm();
            postForm.setId(post.getId());
            postForm.setTitle(post.getTitle());
            postForm.setContent(post.getContent());
            postForm.setCategoryId(Math.toIntExact(post.getCategory().getCategoryId()));
            postForm.setBoardId(Math.toIntExact(post.getCategory().getBoard().getBoardId()));
            postForm.setMemberId(Math.toIntExact(post.getMember().getMemberId()));
            postForm.setMemberEmail(post.getMember().getEmail());
            postForm.setStrHashtag(post.getHashtags().stream()
                    .map(postHashtag -> postHashtag.getHashtag().getName())
                    .collect(Collectors.joining(" ")));

            model.addAttribute("postForm", postForm);

            Long selectedBoardId = post.getCategory().getBoard().getBoardId();
            addCommonAttributes(model, selectedBoardId);

            return "posts/postform"; // 수정 폼 페이지
        }
        return "redirect:/posts/" + post.getId(); // 권한이 없으면 원래 페이지로 리디렉트
    }

    // 게시물 수정 요청을 처리하는 메서드
    @PostMapping("/update/{postId}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long postId,
                             @Valid @ModelAttribute PostForm postForm,
                             BindingResult bindingResult,
                             Principal principal) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "posts/postform";
        }

        Member member = mainController.getCurrentMember();
        Post post = postService.getPostById(postId);
        if (post.getMember().getEmail().equals(member.getEmail())) {
            postService.updatePost(postId, postForm.getTitle(), postForm.getContent());
        }
        return "redirect:/posts/" + post.getId(); // 수정 후 원래 페이지로 리디렉트
    }

    // 게시물 삭제
    @DeleteMapping("/delete/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) throws SQLException {
        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();
        if (post.getMember().getMemberId().equals(member.getMemberId())) {
            postService.deletePost(postId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 권한이 없는 경우
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

    @PostMapping("/bookmark/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> bookmarkPost(@PathVariable Long postId) throws SQLException {
        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();
        int status = bookMarkService.toggleBookMark(post, member);
        if (status == 0) {
            // 북마크가 제거됨
            return ResponseEntity.ok().build();
        } else {
            // 북마크가 추가됨
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
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
        model.addAttribute("boardId", boardId);
        return "posts/searchPosts";
    }

}
