package com.kosa.kmt.post;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CreateTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    @Disabled
    public void testCreate() {
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Post 1 content");
        postRepository.save(post1);
        assertNotNull(post1);
        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Post 2 content");
        postRepository.save(post2);
        assertNotNull(post2);

    }
}
