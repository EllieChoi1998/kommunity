package com.kosa.kmt.post;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Log4j2
class PostRepoTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testFindAll() {
        List<Post> all = this.postRepository.findAll();
        assertEquals(2, all.size());

        Post p = all.get(1);
        assertEquals("Post 2 content", p.getContent());
    }

    @Test
    public void testFindById() {
        Optional<Post> post = postRepository.findById(1L);
        if (post.isPresent()) {
            Post p = post.get();
            assertEquals("Post 1 content", p.getContent());
        }
    }

    @Test
    public void testFindPostDateById(){
        Post p = this.postRepository.findById(1L).get();
        log.info("투스트링" + p.toString());
        assertEquals(1, p.getId());
    }
}