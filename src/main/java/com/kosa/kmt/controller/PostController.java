package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.hashtag.*;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostRepository;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final MainController mainController;
    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepository hashtagRepository;

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
//                        System.out.println("+++++++++++++++++++++++++++++");
//                        System.out.println(foundHashtagDTOS.get(hashtagId).getName() + "\t" + foundHashtagDTOS.get(hashtagId).getCount());
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

        return hashtagDTOS;

    }

    private void addCommonAttributes(Model model, Long boardId) throws SQLException {
        List<Board> boards = boardService.findAllBoards();
        Board board = boardId != null ? boardService.findBoardById(boardId).orElse(null) : null;
        List<Category> categories = boardId != null ? categoryService.findCategoriesByBoardId(boardId) : List.of();

        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));


        List<HashtagDTO> hashtagDTO = findAllHastags_by_boardId(categories, board);
//        System.out.println("*******************");
//        for (HashtagDTO hashtagDTO1 : hashtagDTO) {
//            System.out.println(hashtagDTO1.getName() + "=" + hashtagDTO1.getCount());
//        }
//        System.out.println("*******************");

        List<HashtagDTO> sortedHashtagDTO = hashtagDTO.stream()
                .sorted(Comparator.comparingInt(HashtagDTO::getCount).reversed())
                .collect(Collectors.toList());

        model.addAttribute("member", mainController.getCurrentMember());
        model.addAttribute("boards", boards);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("hashtagDTO", sortedHashtagDTO);
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
