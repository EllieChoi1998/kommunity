package com.kosa.kmt.controller;
import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

//    @GetMapping("/kommunity/main")
//    public String main(Model model) {
//        List<Board> boards = boardService.findAllBoards();
//        Map<Long, List<Category>> boardCategories = boards.stream()
//                .collect(Collectors.toMap(Board::getBoardId, categoryService::findCategoriesByBoard));
//
//        System.out.println("boardCategories: " + boardCategories);
//
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardCategories", boardCategories);
//        return "danamusup";
//    }

    @GetMapping("/kommunity/main")
    public String main(@RequestParam(value = "categoryId", required = false) Long categoryId, Model model) throws 
    {
        List<Board> boards = boardService.findAllBoards();
        Map<Long, List<Category>> boardCategories = boards.stream()
                .collect(Collectors.toMap(Board::getBoardId, categoryService::findCategoriesByBoard));

        Long boardId = 1L; // 익명 게시판의 ID
        Optional<Board> optionalBoard = boards.stream().filter(board -> board.getBoardId().equals(boardId)).findFirst();

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

        model.addAttribute("member", getCurrentMember());
        return "danamusup";
    }

    private Member getCurrentMember() {
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
                    System.out.println(member.getMemberId());
                    System.out.println(member.getEmail());
                    System.out.println(member.getName());
                    System.out.println(member.getNickname()); // 닉네임 출력
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
                System.out.println("로그인 시간 업데이트 실패");
                model.addAttribute("error", "로그인 시간 업데이트에 실패했습니다.");
                break;
            case -1:
                System.out.println("비밀번호 불일치");
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                break;

            case -2:
                System.out.println("이메일 없음");
                model.addAttribute("error", "이메일을 찾을 수 없습니다.");
                System.out.println(model.getAttribute("error"));
                break;

            default:
                System.out.println("Unexpected Error");
                model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
                break;

        }
        return "redirect:/kommunity";
    }


}
