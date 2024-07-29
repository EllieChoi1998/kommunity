package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.comment.PostCommentRepository;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class OrderByCommentsTests {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        postCommentRepository.deleteAll();
        postRepository.deleteAll();

        // 데이터 생성
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@gmail.com");
        member.setNickname("JD");
        member.setPassword("1234");
        member = this.memberRepository.save(member);

        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Content 1");
        post1.setMember(member);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Content 2");
        post2.setMember(member);
        postRepository.save(post2);

        Post post3 = new Post();
        post3.setTitle("Post 3");
        post3.setContent("Content 3");
        post3.setMember(member);
        postRepository.save(post3);

        PostComment comment1 = new PostComment();
        comment1.setCommentContent("Comment 1");
        comment1.setCommentDateTime(LocalDateTime.now());
        comment1.setMember(member);
        comment1.setPost(post1);
        postCommentRepository.save(comment1);

        PostComment comment2 = new PostComment();
        comment2.setCommentContent("Comment 2");
        comment2.setCommentDateTime(LocalDateTime.now());
        comment2.setMember(member);
        comment2.setPost(post1);
        postCommentRepository.save(comment2);

        PostComment comment3 = new PostComment();
        comment3.setCommentContent("Comment 3");
        comment3.setCommentDateTime(LocalDateTime.now());
        comment3.setMember(member);
        comment3.setPost(post2);
        postCommentRepository.save(comment3);
    }
    @Test
    public void testGetPostsOrderByCommentsDesc() throws Exception {
        // When
        List<Post> posts = postService.getPostsOrderByCommentsDesc();

        // Then
        assertNotNull(posts);
        assertEquals(3, posts.size());
        assertEquals("Post 1", posts.get(0).getTitle()); // 댓글 2개
        assertEquals("Post 2", posts.get(1).getTitle()); // 댓글 1개
        assertEquals("Post 3", posts.get(2).getTitle()); // 댓글 0개
    }

}
