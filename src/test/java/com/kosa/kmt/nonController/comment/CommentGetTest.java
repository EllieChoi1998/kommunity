package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CommentGetTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private PostCommentRepository postCommentRepository;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setId(13L);

        PostComment comment1 = new PostComment();
        comment1.setCommentContent("Test Comment 1");
        comment1.setPost(post);

        PostComment comment2 = new PostComment();
        comment2.setCommentContent("Test Comment 2");
        comment2.setPost(post);

        List<PostComment> comments = Arrays.asList(comment1, comment2);

        given(postCommentRepository.findByPostId(13L)).willReturn(comments);
    }

    @Test
    void testGetCommentsByPostId() {
        List<PostComment> comments = commentService.getCommentsByPostId(13L);
        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getCommentContent()).isEqualTo("Test Comment 1");
        assertThat(comments.get(1).getCommentContent()).isEqualTo("Test Comment 2");
    }
}