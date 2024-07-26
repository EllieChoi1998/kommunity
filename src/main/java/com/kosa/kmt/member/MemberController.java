package com.kosa.kmt.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/profile-menu")
    public String profileMenu() {
        return "profile-menu";
    }
}
