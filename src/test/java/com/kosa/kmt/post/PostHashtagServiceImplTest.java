package com.kosa.kmt.post;

import com.kosa.kmt.hashtag.HashtagRepository;
import com.kosa.kmt.hashtag.PostHashtag;
import com.kosa.kmt.hashtag.PostHashtagRepository;
import com.kosa.kmt.hashtag.PostHashtagServiceImpl;
import com.kosa.kmt.post.Post;
import com.kosa.kmt.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostHashtagServiceImplTest {

    @Autowired
    private PostHashtagServiceImpl postHashtagService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    private Post post;

    @BeforeEach
    public void setUp() {
        post = new Post();
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setMemberId(1);
        post.setCategoryId(1);
        postRepository.save(post);
    }

    @Test
    @Transactional
    public void testSetHashtag() {
        String hashtags = "#java #spring";

        postHashtagService.setHashtag(post, hashtags);

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost_Id(post.getId());
        assertEquals(2, postHashtags.size());

        assertTrue(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("java")));
        assertTrue(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("spring")));
    }

    @Test
    @Transactional
    public void testUpdateHashtag() {
        String initialHashtags = "#java #spring";
        postHashtagService.setHashtag(post, initialHashtags);

        String updatedHashtags = "#java #springboot";
        postHashtagService.setHashtag(post, updatedHashtags);

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost_Id(post.getId());
        assertEquals(2, postHashtags.size());

        assertTrue(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("java")));
        assertTrue(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("springboot")));
        assertFalse(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("spring")));
    }

    @Test
    @Transactional
    public void testRemoveHashtag() {
        String initialHashtags = "#java #spring";
        postHashtagService.setHashtag(post, initialHashtags);

        String updatedHashtags = "#java";
        postHashtagService.setHashtag(post, updatedHashtags);

        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost_Id(post.getId());
        assertEquals(1, postHashtags.size());

        assertTrue(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("java")));
        assertFalse(postHashtags.stream().anyMatch(ph -> ph.getHashtag().getName().equals("spring")));
    }
}