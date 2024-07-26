package com.kosa.kmt.nonController.member.signup.oAuth;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//        // 사용자 정보를 세션에 저장
//        request.getSession().setAttribute("username", oAuth2User.getEmail());
        response.sendRedirect("/kommunity/setInfo");
    }


}
