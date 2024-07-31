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
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostRepository;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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

    private final MarkdownService markdownService;

    public static List<HashtagDTO> staticHashtagDTOs;


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
        model.addAttribute("selectedBoardId", boardId); // 추가된 부분
        model.addAttribute("sortedHashtagDTO", sortedHashtagDTO);
    }


    @GetMapping("/board/{boardId}")
    public String getPostsByBoard(@PathVariable Long boardId, Model model) throws SQLException {
        Optional<Board> optionalBoard = boardService.findBoardById(boardId);
        if (optionalBoard.isEmpty()) {
            return "error/404";
        }

        Board board = optionalBoard.get();
        List<Post> posts = postService.getPostsByBoard(boardId);

        List<PostForm> postForms = posts.stream().map(post -> {
            PostForm postForm = new PostForm();
            postForm.setId(post.getId());
            postForm.setTitle(post.getTitle());
            postForm.setContent(post.getContent());
            Node document = Parser.builder().build().parse(post.getContent());
            postForm.setRenderedContent(HtmlRenderer.builder().build().render(document));
            postForm.setMemberId(post.getMember().getMemberId());
            postForm.setNickname(post.getMember().getNickname());
            postForm.setCategoryId(Math.toIntExact(post.getCategory().getCategoryId()));
            postForm.setBoardId(Math.toIntExact(post.getCategory().getBoard().getBoardId()));
            postForm.setPostDate(post.getPostDate());
            postForm.setHashtags(postHashtagRepository.findAllByPost_Id(post.getId()));
            return postForm;
        }).toList();

        addCommonAttributes(model, boardId);

        model.addAttribute("board", board);
        model.addAttribute("posts", postForms);
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

        List<PostForm> postForms = posts.stream().map(post -> {
            PostForm postForm = new PostForm();
            postForm.setId(post.getId());
            postForm.setTitle(post.getTitle());
            postForm.setContent(post.getContent());
            Node document = Parser.builder().build().parse(post.getContent());
            postForm.setRenderedContent(HtmlRenderer.builder().build().render(document));
            postForm.setMemberId(post.getMember().getMemberId());
            postForm.setNickname(post.getMember().getNickname());
            postForm.setCategoryId(Math.toIntExact(post.getCategory().getCategoryId()));
            postForm.setBoardId(Math.toIntExact(post.getCategory().getBoard().getBoardId()));
            postForm.setPostDate(post.getPostDate());
            postForm.setHashtags(postHashtagRepository.findAllByPost_Id(post.getId()));
            return postForm;
        }).toList();

        addCommonAttributes(model, boardId);

        model.addAttribute("category", category);
        model.addAttribute("posts", postForms);
        model.addAttribute("isAllCategories", false);
        return "posts/posts";
    }
}
