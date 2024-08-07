package com.kosa.kmt.controller;
import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.hashtag.Hashtag;
import com.kosa.kmt.nonController.hashtag.HashtagRepository;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Controller
public class MainController {
    private final MemberService memberService;
    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public MainController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/kommunity")
    public String index(Model model) {
        model.addAttribute("loginForm", new Member()); // 폼 객체 추가
        return "index"; // Thymeleaf 템플릿 파일을 찾기 위한 설정
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/kommunity"; // 리디렉션 설정
    }


    @GetMapping("/kommunity/main")
    public String main(@RequestParam(value = "categoryId", required = false) Long categoryId,
                       @RequestParam(value = "selectedBoardId", required = false) Long selectedBoardId,
                       Model model) throws Exception {
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

        // 오류가 나는 경우 보드 이름을 통해 검색 부분 없애고 아래 부분 넣을 것
        // Long boardId = selectedBoardId != null ? selectedBoardId : 1L; // 기본적으로 익명 게시판의 ID
        // Optional<Board> optionalBoard = boards.stream().filter(board -> board.getBoardId().equals(boardId)).findFirst();

        List<Post> posts;
        if (categoryId != null) {
            posts = postService.getPostsByCategory(categoryId);
        } else {
            List<Category> categories = categoryService.findCategoriesByBoardId(boardId);
            if (!categories.isEmpty()) {
                posts = postService.getPostsByCategory(categories.get(0).getCategoryId());
            } else {
                posts = List.of(); // 카테고리가 없으면 빈 리스트 반환
            }
        }

        model.addAttribute("boards", boards);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("selectedBoardId", boardId);
        model.addAttribute("member", getCurrentMember());
        model.addAttribute("leftSidebar", false);
        model.addAttribute("rightSidebar", false);

        List<Hashtag> hashtags = hashtagRepository.findAll();
        Map<String, Integer> hashtagCount = new HashMap<>();
        for (Hashtag hashtag : hashtags) {
            hashtagCount.put(hashtag.getName(), hashtagCount.getOrDefault(hashtag.getName(), 0) + 1);
        }
        model.addAttribute("hashtags", hashtagCount);

        return "danamusup";
    }

    protected Member getCurrentMember() {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();

                Member member = memberRepository.findByEmail(username).orElse(null);
                if (member != null) {

                    return member;
                }

            }
        }
        return null;
    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new Member());
        return "index"; // 템플릿 파일 이름 (index.html)
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) throws Exception {
        Integer result = memberService.login(email, password);

        switch (result) {
            case 1:
                return "redirect:/home"; // 로그인 성공 시 리다이렉트 경로
            case 0:

                model.addAttribute("error", "로그인 시간 업데이트에 실패했습니다.");
                break;
            case -1:

                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                break;

            case -2:

                model.addAttribute("error", "이메일을 찾을 수 없습니다.");
                System.out.println(model.getAttribute("error"));
                break;

            default:

                model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
                break;

        }
        return "redirect:/kommunity";
    }

    // addCommonAttributes 메서드 추가
    public void addCommonAttributes(Model model, Long boardId) throws SQLException {
        List<Board> boards = boardService.findAllBoards();
        Board board = boardId != null ? boardService.findBoardById(boardId).orElse(null) : null;
        List<Category> categories = boardId != null ? categoryService.findCategoriesByBoardId(boardId) : List.of();

        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, b -> categoryService.findCategoriesByBoardId(b.getBoardId())));

        model.addAttribute("member", getCurrentMember());
        model.addAttribute("boards", boards);
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        model.addAttribute("boardCategories", boardCategories);
        model.addAttribute("selectedBoardId", boardId); // selectedBoardId 추가
    }
}
