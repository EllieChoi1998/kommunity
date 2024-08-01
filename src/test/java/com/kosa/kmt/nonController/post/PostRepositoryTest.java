package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardRepository;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Board board;
    private Category category;

    @BeforeEach
    public void setUp() {
        board = new Board();
        board.setName("Test Board");
        boardRepository.saveBoard(board);

        category = new Category();
        category.setName("Test Category");
        category.setBoard(board);
        categoryRepository.saveCategory(category);

        Post post1 = new Post();
        post1.setTitle("First Post");
        post1.setContent("First Content");
        post1.setCategory(category);
        post1.setPostDate(LocalDateTime.now().minusDays(1));
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Second Post");
        post2.setContent("Second Content");
        post2.setCategory(category);
        post2.setPostDate(LocalDateTime.now());
        postRepository.save(post2);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_Board_BoardIdOrderByPostDateDesc() {
        List<Post> posts = postRepository.findAllByCategory_Board_BoardIdOrderByPostDateDesc(board.getBoardId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getTitle()).isEqualTo("Second Post");
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_Board_BoardIdOrderByPostDateAsc() {
        List<Post> posts = postRepository.findAllByCategory_Board_BoardIdOrderByPostDateAsc(board.getBoardId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getTitle()).isEqualTo("First Post");
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_CategoryIdOrderByPostDateDesc() {
        List<Post> posts = postRepository.findAllByCategory_CategoryIdOrderByPostDateDesc(category.getCategoryId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getTitle()).isEqualTo("Second Post");
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_CategoryIdOrderByPostDateAsc() {
        List<Post> posts = postRepository.findAllByCategory_CategoryIdOrderByPostDateAsc(category.getCategoryId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.get(0).getTitle()).isEqualTo("First Post");
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_Board_BoardId() {
        List<Post> posts = postRepository.findAllByCategory_Board_BoardId(board.getBoardId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllByCategory_CategoryId() {
        List<Post> posts = postRepository.findAllByCategory_CategoryId(category.getCategoryId());
        assertThat(posts).isNotEmpty();
        assertThat(posts.size()).isEqualTo(2);
    }
}