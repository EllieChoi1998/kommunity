package com.kosa.kmt.post;

import com.kosa.kmt.non_controller.hashtag.PostHashtagService;
import com.kosa.kmt.non_controller.post.Post;
import com.kosa.kmt.non_controller.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PostServiceTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostHashtagService postHashtagService;

    @Test
    public void testGetPostsAll() throws SQLException {
        List<Post> posts = postService.getPostsAll();
        assertEquals(13, posts.size());
    }

    @Test
    public void testGetPostById() throws SQLException {
        Post p = postService.getPostById(1L);
        assertEquals("title : [001]", p.getTitle());
    }

    @Test
    @Transactional
    public void testUpdatePost() throws SQLException {
        Post p = this.postService.getPostById(1L);
        p.setContent("서비스에서 내용을 변경합니다.");
        this.postService.updatePost(p, 1L);
    }

    @Test
    @Transactional
    public void testDeletePost() throws SQLException {
        this.postService.deletePost(12L);
    }



}
