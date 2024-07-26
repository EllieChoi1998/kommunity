package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.comment.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTests {

    @Autowired
    private CommentService commentService;

    @Test
    public void testAddComment() {

    }
}
