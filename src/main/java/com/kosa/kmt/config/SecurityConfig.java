package com.kosa.kmt.config;

import com.kosa.kmt.nonController.member.signup.oAuth.CustomOAuth2LoginSuccessHandler;
import com.kosa.kmt.nonController.member.signup.oAuth.CustomOAuth2UserService;
import com.kosa.kmt.nonController.member.signup.UserRole;
import com.kosa.kmt.nonController.member.signup.UserSecurityService; // UserSecurityService 추가
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserSecurityService userSecurityService; // UserSecurityService 추가

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/login",
                                "/kommunity/signup",
                                "/kommunity/validateEmail",
                                "/kommunity/validateName",
                                "/kommunity/setInfo",
                                "/kommunity/finish",
                                "/kommunity/sendEmail",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/*.css",
                                "/*.js",
                                "/*.png",
                                "/*.jpg",
                                "/*.jpeg",
                                "/*.svg"
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").invalidateHttpSession(true))
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/kommunity/validateName")
                        .successHandler(new CustomOAuth2LoginSuccessHandler())
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)));

        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 권한에 따라 리디렉션 URL 설정
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.ADMIN.getValue()))) {
                response.sendRedirect("/kommunity/main");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.USER.getValue()))) {
                response.sendRedirect("/kommunity/main");
            }
        };
    }
}
