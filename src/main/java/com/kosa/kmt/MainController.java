package com.kosa.kmt;

import com.kosa.kmt.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/kommunity")
    public String index(Model model) {
        model.addAttribute("loginForm", new Member()); // 폼 객체 추가
        return "index"; // Thymeleaf 템플릿 파일을 찾기 위한 설정
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/kommunity"; // 리디렉션 설정
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new Member());
        return "index"; // 템플릿 파일 이름 (index.html)
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute Member member) {
        // 로그인 처리 로직
        return "redirect:/home"; // 로그인 성공 시 리다이렉트 경로
    }
}
