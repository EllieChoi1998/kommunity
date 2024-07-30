//package com.kosa.kmt.config;
//
//import jakarta.servlet.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.io.IOException;
//
//public class CustomFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("CustomFilter.doFilter");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("authentication.getCredentials = " + authentication.getCredentials());
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
