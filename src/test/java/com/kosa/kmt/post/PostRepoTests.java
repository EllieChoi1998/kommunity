package com.kosa.kmt.post;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Log4j2
class PostRepoTests {

    @Autowired
    private PostRepository postRepository;

    // read all
    @Test
    public void testFindAll() {
        List<Post> all = this.postRepository.findAll();
        assertEquals(2, all.size());

        Post p = all.get(1);
        assertEquals("Post 2 content", p.getContent());
    }

    // read one
    @Test
    public void testFindById() {
        Optional<Post> post = postRepository.findById(1L);
        if (post.isPresent()) {
            Post p = post.get();
            assertEquals("Post 1 content", p.getContent());
        }
    }

    // read date by id
    @Test
    public void testFindPostDateById(){
        Post p = this.postRepository.findById(1L).get();
        assertEquals(LocalDate.of(2024, 7, 19).getMonth(), p.getPostDate().getMonth());
        assertEquals(LocalDate.of(2024, 7, 19).getDayOfMonth(), p.getPostDate().getDayOfMonth());
    }

    // update by id
    @Test
    public void testUpdatePostById(){
        Optional<Post> op = this.postRepository.findById(1L);
        assertTrue(op.isPresent());
        Post p = op.get();
        p.setContent("내용을 변경합니다.");
        this.postRepository.save(p);
    }

    // delete by id
    @Test
    @Transactional
    public void testDeletePostById(){
        Optional<Post> op = this.postRepository.findById(4L);
        assertTrue(op.isPresent());
        Post p = op.get();
        this.postRepository.delete(p);
    }
}