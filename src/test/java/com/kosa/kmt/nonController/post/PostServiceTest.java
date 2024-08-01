package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryRepository;
import com.kosa.kmt.nonController.comment.PostCommentRepository;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostService postService;

    private Long boardId;
    private Long categoryId;

    @BeforeEach
    public void setUp() {
        boardId = 1L;
        categoryId = 1L;
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Post 1 content");
        post1.setMember(memberRepository.findById(1).get());
        post1.setCategory(categoryRepository.findById(categoryId).get());
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Post 2 content");
        post2.setMember(memberRepository.findById(1).get());
        post2.setCategory(categoryRepository.findById(categoryId).get());
        postRepository.save(post2);

    }

    @Test
    @Transactional
    public void testGetPostsByBoardOrderByPostDateDesc() throws SQLException {
        List<Post> posts = postService.getPostsByBoardOrderByPostDateDesc(boardId);
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getPostDate()).isAfterOrEqualTo(posts.get(1).getPostDate());
    }

    @Test
    @Transactional
    public void testGetPostsByBoardOrderByPostDateAsc() throws SQLException {
        List<Post> posts = postService.getPostsByBoardOrderByPostDateAsc(boardId);
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getPostDate()).isBeforeOrEqualTo(posts.get(1).getPostDate());
    }

    @Test
    @Transactional
    public void testGetPostsByBoardOrderByBookmarksDesc() throws SQLException {
        List<Post> posts = postService.getPostsByBoardOrderByBookmarksDesc(boardId);
        assertThat(posts).isNotEmpty();
        // Add assertions to verify the order by bookmark count
    }

    @Test
    @Transactional
    public void testGetPostsByBoardOrderByCommentsDesc() throws SQLException {
        List<Post> posts = postService.getPostsByBoardOrderByCommentsDesc(boardId);
        assertThat(posts).isNotEmpty();
        // Add assertions to verify the order by comment count
    }

    @Test
    @Transactional
    public void testGetPostsByCategoryOrderByPostDateDesc() throws SQLException {
        List<Post> posts = postService.getPostsByCategoryOrderByPostDateDesc(categoryId);
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getPostDate()).isAfterOrEqualTo(posts.get(1).getPostDate());
    }

    @Test
    @Transactional
    public void testGetPostsByCategoryOrderByPostDateAsc() throws SQLException {
        List<Post> posts = postService.getPostsByCategoryOrderByPostDateAsc(categoryId);
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getPostDate()).isBeforeOrEqualTo(posts.get(1).getPostDate());
    }

    @Test
    @Transactional
    public void testGetPostsByCategoryOrderByBookmarksDesc() throws SQLException {
        List<Post> posts = postService.getPostsByCategoryOrderByBookmarksDesc(categoryId);
        assertThat(posts).isNotEmpty();
        // Add assertions to verify the order by bookmark count
    }

    @Test
    @Transactional
    public void testGetPostsByCategoryOrderByCommentsDesc() throws SQLException {
        List<Post> posts = postService.getPostsByCategoryOrderByCommentsDesc(categoryId);
        assertThat(posts).isNotEmpty();
        // Add assertions to verify the order by comment count
    }
}
