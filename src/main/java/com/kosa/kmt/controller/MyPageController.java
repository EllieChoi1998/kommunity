package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.hashtag.PostHashtagRepository;
import com.kosa.kmt.nonController.post.*;
import com.kosa.kmt.nonController.post.bookmark.BookMark;
import com.kosa.kmt.nonController.post.bookmark.BookMarkService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/kommunity/mypage")
public class MyPageController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;

    @Autowired
    BookMarkService bookMarkService;
    @Autowired
    private MainController mainController;
    @Autowired
    private PostHashtagRepository postHashtagRepository;
    @Autowired
    private PostRepository postRepository;

    private void commons(Model model, Long selectedBoardId) throws Exception {
        List<Board> boards = boardService.findAllBoards();
        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, categoryService::findCategoriesByBoard));

        // 대나무숲 보드를 찾기 위해 보드 이름을 통해 검색 (대나무 숲 보드Id가 1이 아닐 경우가 있기에)
        Long bambooBoardId = boards.stream()
                .filter(board -> "대나무숲".equals(board.getName()))
                .findFirst()
                .map(Board::getBoardId)
                .orElse(null);

        Long boardId = selectedBoardId != null ? selectedBoardId : bambooBoardId;
        if (boardId == null) {
            throw new IllegalStateException("대나무숲 보드를 찾을 수 없습니다.");
        }

        model.addAttribute("boards", boards);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("selectedBoardId", boardId);
        model.addAttribute("member", mainController.getCurrentMember());

    }

    private List<PostForm> convertPostToPostForm(List<Post> posts) {
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

        return postForms;
    }

    @GetMapping("/bookmark")
    public String mine(@RequestParam(value = "selectedBoardId", required = false) Long selectedBoardId,
                       Model model) throws Exception {

        commons(model, selectedBoardId);

        List<BookMark> bookMarks = bookMarkService.getBookMarksByMember(mainController.getCurrentMember());

        List<Post> posts = new ArrayList<>();

        for (BookMark bookMark : bookMarks) {
            posts.add(bookMark.getPost());
        }

        model.addAttribute("posts", convertPostToPostForm(posts));

        return "mypage/bookmark";
    }

    @GetMapping("/myposts")
    public String myposts(@RequestParam(value = "selectedBoardId", required = false) Long selectedBoardId, Model model) throws Exception {
        commons(model, selectedBoardId);

        List<Post> allPosts = postService.getPostsAll();
        List<Post> myPosts = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getMember().equals(mainController.getCurrentMember())) {
                myPosts.add(post);
            }
        }

        model.addAttribute("posts", convertPostToPostForm(myPosts));
        return "mypage/myposts";
    }
}
