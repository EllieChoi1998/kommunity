package com.kosa.kmt.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleController {

    @GetMapping("/getUserRole")
    public ResponseEntity<String> getUserRole(HttpSession session) {

        System.out.println("USER ROLE HAS BEEN CALLED !!!!!");

        // SecurityContextHolder를 통해 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authentication 객체에서 UserDetails 객체 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // UserDetails 객체에서 이메일 정보 가져오기
        String email = userDetails.getUsername(); // getUsername() 메서드는 UserDetails 인터페이스에서 이메일을 반환

        String role = userDetails.getAuthorities().toString();
        String password = userDetails.getPassword();

        // 세션에서 사용자 역할을 가져옴 (예: "ROLE_ADMIN", "ROLE_USER")
        String userRole = (String) session.getAttribute("USER_ROLE");

        System.out.println("ROLE IS : " + role + "\t" + userRole);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
