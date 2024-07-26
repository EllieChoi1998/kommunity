package com.kosa.kmt.config;

import com.kosa.kmt.non_controller.board.BoardRepository;
import com.kosa.kmt.non_controller.board.BoardRepositoryImpl;
import com.kosa.kmt.non_controller.board.BoardService;
import com.kosa.kmt.non_controller.board.BoardServiceImpl;
import com.kosa.kmt.non_controller.category.*;
import com.kosa.kmt.non_controller.category.CategoryRepository;
import com.kosa.kmt.non_controller.category.CategoryRepositoryImpl;
import com.kosa.kmt.non_controller.category.CategoryService;
import com.kosa.kmt.non_controller.category.CategoryServiceImpl;
import com.kosa.kmt.non_controller.chat.ChatRepository;
import com.kosa.kmt.non_controller.chat.ChatRepositoryImpl;
import com.kosa.kmt.non_controller.chat.ChatService;
import com.kosa.kmt.non_controller.chat.ChatServiceImpl;
import com.kosa.kmt.non_controller.member.MemberRepository;
import com.kosa.kmt.non_controller.member.MemberRepositoryImpl;
import com.kosa.kmt.non_controller.member.MemberService;
import com.kosa.kmt.non_controller.member.MemberServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final EntityManager em;

    @Autowired
    public AppConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryImpl(em);
    }

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public ChatRepository chatRepository() {
        return new ChatRepositoryImpl(em);
    }

    @Bean
    public ChatService chatService() {
        return new ChatServiceImpl(chatRepository(), memberRepository());
    }

    @Bean
    public BoardRepository boardRepository() { return new BoardRepositoryImpl(em); }

    @Bean
    public BoardService boardService() { return new BoardServiceImpl(boardRepository()); }

    @Bean
    public CategoryRepository categoryRepository() { return new CategoryRepositoryImpl(em); }

    @Bean
    public CategoryService categoryService() { return new CategoryServiceImpl(categoryRepository(), boardRepository()); }
}

