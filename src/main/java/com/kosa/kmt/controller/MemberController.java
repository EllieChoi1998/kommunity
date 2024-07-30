package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @GetMapping("/profile-menu")
    public String profileMenu() {
        return "profile-menu";
    }

    @GetMapping("/mypage-menu")
    public String mypageMenu(Model model) {
        Member member = getCurrentMember();
        model.addAttribute("member", member);
        return "mypage/mypage";
    }

    @PostMapping("/update-nickname")
    @ResponseBody
    public Map<String, Object> updateNickname(@RequestParam String nickname) {
        Member member = getCurrentMember();
        Map<String, Object> response = new HashMap<>();
        if (member != null) {
            member.setNickname(nickname);
            memberService.save(member);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "닉네임 변경에 실패했습니다.");
        }
        return response;
    }

    @PostMapping("/update-password")
    @ResponseBody
    public Map<String, Object> updatePassword(@RequestParam String newPassword) {
        Member member = getCurrentMember();
        Map<String, Object> response = new HashMap<>();
        if (member != null) {
            memberService.updatePassword(member, newPassword);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "비밀번호 변경에 실패했습니다.");
        }
        return response;
    }

    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                return memberRepository.findByEmail(username).orElse(null);
            }
        }
        return null;
    }
}
