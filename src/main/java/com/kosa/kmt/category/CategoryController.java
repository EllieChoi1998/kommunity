package com.kosa.kmt.category;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category/danamusup")
    public String danamusup() {
        return "category-danamusup"; // category-danamusup.html 페이지를 반환합니다.
    }

    @GetMapping("/category/codingtest")
    public String codingtest() {
        return "category-codingtest"; // category-codingtest.html 페이지를 반환합니다.
    }

    @GetMapping("/category/jobtips")
    public String jobtips() {
        return "category-jobtips"; // category-jobtips.html 페이지를 반환합니다.
    }
}
