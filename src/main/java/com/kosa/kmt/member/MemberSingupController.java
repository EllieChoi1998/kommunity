package com.kosa.kmt.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLOutput;
import java.util.Optional;

@Controller
@RequestMapping("/kommunity")
public class MemberSingupController {

    @Autowired
    MemberService memberService;

    Member member;

    static String emailAddress;
    static String authCode;
    @Qualifier("memberRepository")
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/signup")
    public String signup() {
        return "signup/main";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String email, Model model){
        Boolean sendSuccess = this.sendCodeToEmail(email);
        if(sendSuccess){
            return "signup/validateEmail";
        }
        return "redirect:/signup";
    }

    private Boolean sendCodeToEmail(String email) {
        String sentCode = memberService.sendCodeToEmail(email);
        if (sentCode.equals("-1")){
            return false;
        } else {
            emailAddress = email;
            authCode = sentCode;
            System.out.println("authCode: " + authCode);
            System.out.println("email: " + email);
            return true;
        }
    }

    @GetMapping("/validateEmail")
    public String validateEmail(){
        return "signup/validateEmail";
    }

    @PostMapping("/validateEmail")
    public String validateEmail(@RequestParam String code, Model model){
        if (authCode.equals(code)){

            return "signup/validateName";
        } else {
            return "redirect:/validateEmail";
        }

    }

    @PostMapping("/sendEmail")
    public String sendEmail(Model model){
        Boolean sendSuccess = this.sendCodeToEmail(emailAddress);
        if(sendSuccess){
            System.out.println("Sent message again");
            return "signup/validateEmail";
        }
        return "redirect:/signup";
    }

    @GetMapping("/validateName")
    public String validateName(){

        return "signup/validateName";
    }

    @GetMapping("/setInfo")
    public String setInfo(Model model){
        member = currentMember();
        model.addAttribute("member", member);
        return "signup/setInfo";
    }

    private Member currentMember(){
        Integer memberId = memberService.findSameEmail(emailAddress);
        Optional<Member> OptionalMember = memberRepository.findById(memberId);
        if(OptionalMember.isPresent()) {
            Member member = OptionalMember.get();

            return member;
        }
        return null;
    }

    @PostMapping("/setInfo")
    public String setInfo(@RequestParam String nickname, @RequestParam String password, Model model){
        member = currentMember();
        memberService.updateNickname(member, nickname);

        System.out.println("password: " + password);

        memberService.updatePassword(member, password);

        model.addAttribute("member", member);
        return "signup/finish";
    }

    @GetMapping("/finish")
    public String finish (Model model){
        return "signup/finish";
    }
}
