package com.kosa.kmt.hashtag;

import com.kosa.kmt.post.Post;
import com.kosa.kmt.post.PostRepository;
import com.kosa.kmt.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HashtagTests {

    @Autowired
    private PostHashtagService postHashtagService;

    @Autowired
    private PostService postService;


    // get hashtags in post
    @Test
    public void testGetHashtagByPost() throws SQLException {
        Post post = this.postService.getPostById(1L);
        List<PostHashtag> hashtags = postHashtagService.getPostHashtags(post);
        assertEquals(2, hashtags.size());
    }

    // update hashtag
    @Test
    public void testUpdateHashtag() throws SQLException {
        String hashtagStr = "# ";
        Post post = this.postService.getPostById(1L);
        postHashtagService.setHashtag(post, hashtagStr);

    }


}
