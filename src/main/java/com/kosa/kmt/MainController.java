package com.kosa.kmt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/kommunity")
    public String index() {
        return "index"; // Thymeleaf 템플릿 파일을 찾기 위한 설정
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/kommunity"; // 리디렉션 설정
    }
}
