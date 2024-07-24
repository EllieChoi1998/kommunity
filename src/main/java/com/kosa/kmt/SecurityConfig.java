package com.kosa.kmt;

import com.kosa.kmt.member.CustomOAuth2LoginSuccessHandler;
import com.kosa.kmt.member.CustomOAuth2UserService;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/kommunity/main"))
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/kommunity/validateName")
                        .successHandler(new CustomOAuth2LoginSuccessHandler())
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)));
                ;
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
//    @Bean
//    public CustomOAuth2UserService customOAuth2UserService() {
//        return new CustomOAuth2UserService();
//    }
}
