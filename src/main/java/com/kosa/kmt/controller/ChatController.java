package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.member.Member;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class ChatController {

    @GetMapping("/chat")
    public void chat(Model model) {

        Member user = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        System.out.println("==================================");
        System.out.println("@ChatController, GET Chat / Username : " + user.getName());

        model.addAttribute("userid", user.getName());
    }
}