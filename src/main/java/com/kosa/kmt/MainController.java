package com.kosa.kmt;

import com.kosa.kmt.member.Member;
import com.kosa.kmt.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final MemberService memberService;

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
    public String main() {
        return "danamusup";
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
