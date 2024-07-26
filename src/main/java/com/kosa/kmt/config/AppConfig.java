package com.kosa.kmt.config;

import com.kosa.kmt.nonController.board.BoardRepository;
import com.kosa.kmt.nonController.board.BoardRepositoryImpl;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.board.BoardServiceImpl;
import com.kosa.kmt.nonController.category.CategoryRepository;
import com.kosa.kmt.nonController.category.CategoryRepositoryImpl;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.category.CategoryServiceImpl;
import com.kosa.kmt.nonController.chat.ChatRepository;
import com.kosa.kmt.nonController.chat.ChatRepositoryImpl;
import com.kosa.kmt.nonController.chat.ChatService;
import com.kosa.kmt.nonController.chat.ChatServiceImpl;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberRepositoryImpl;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.member.MemberServiceImpl;
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

