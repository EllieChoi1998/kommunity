package com.kosa.kmt.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepoTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testCreate() {
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Post 1 content");
        postRepository.save(post1);
        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Post 2 content");
        postRepository.save(post2);
    }
}