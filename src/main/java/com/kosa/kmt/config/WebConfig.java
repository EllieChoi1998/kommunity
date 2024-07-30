package com.kosa.kmt.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/kommunity/**")
//                .allowedOrigins("http://localhost:8083") // 모든 도메인 허용
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
    @Bean
    public CorsConfigurationSource getCorsConfiguration() {
        List<String> originList = new ArrayList<>();
        originList.add("http://localhost:8083");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(originList);
        List<String> methodList = List.of("GET", "POST", "PUT", "DELETE", "HEAD");
        corsConfiguration.setAllowedMethods(methodList);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}

