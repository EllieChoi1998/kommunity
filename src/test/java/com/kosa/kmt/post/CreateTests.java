package com.kosa.kmt.post;

import com.kosa.kmt.hashtag.PostHashtagRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CreateTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    // create
//    @Test
//    public void createPostRepo() {
//        Post post1 = new Post();
//        post1.setTitle("Post 1");
//        post1.setContent("Post 1 content");
//        postRepository.save(post1);
//        assertNotNull(post1);
//
//        Post post2 = new Post();
//        post2.setTitle("Post 2");
//        post2.setContent("Post 2 content");
//        postRepository.save(post2);
//        assertNotNull(post2);
//    }

    @Test
    public void createPostLittle() throws SQLException {
        postService.createPost("title", "content", 1, 1, "#java #html");
    }

    @Test
    public void createPostService() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            String title = String.format("title : [%03d]", i);
            String content = String.format("content : [%03d]", i);
            String hashtag;
            if(i % 3 == 0){
                hashtag = String.format("#자바 #java");
            } else if(i % 3 == 1) {
                hashtag = String.format("#파이썬 #python");
            } else {
                hashtag = String.format("#익명 #대나무숲");
            }

            this.postService.createPost(title, content, 1, 1, hashtag);
        }
    }
}
