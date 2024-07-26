package com.kosa.kmt.hashtag;

import com.kosa.kmt.non_controller.hashtag.PostHashtag;
import com.kosa.kmt.non_controller.hashtag.PostHashtagService;
import com.kosa.kmt.non_controller.post.Post;
import com.kosa.kmt.non_controller.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Post post = this.postService.getPostById(24L);
        List<PostHashtag> hashtags = postHashtagService.getPostHashtags(post);
        assertEquals(2, hashtags.size());
    }

    // update hashtag
    @Test
    public void testUpdateHashtag() throws SQLException {
        String hashtagStr = "#수정한 #해시태그를 #나눠주세요 ";
        Post post = this.postService.getPostById(24L);
        postHashtagService.setHashtag(post, hashtagStr);
    }


}
