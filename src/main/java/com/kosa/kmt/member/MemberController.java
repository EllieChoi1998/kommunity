package com.kosa.kmt.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kommunity")
public class MemberController {

    @GetMapping("/signup")
    public String signup() {
        return "signup/main";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String email, Model model){
        return "signup/validateEmail";
    }

    @GetMapping("/validateEmail")
    public String validateEmail(){
        return "signup/validateEmail";
    }

    @PostMapping("/validateEmail")
    public String validateEmail(@RequestParam String code, Model model){
        return "signup/validateName";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String email){
        return "signup/validateEmail";
    }

    @GetMapping("/validateName")
    public String validateName(){
        return "signup/validateEmail";
    }
}
