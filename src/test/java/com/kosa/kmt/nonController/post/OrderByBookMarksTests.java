package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.post.bookmark.BookMark;
import com.kosa.kmt.nonController.post.bookmark.BookMarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class OrderByBookMarksTests {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Test
    public void testGetPostsOrderByBookmarksDesc() throws Exception {
        // Given
        Member member = new Member();
        member.setName("Test User");
        member.setEmail("testuser@example.com");
        member = memberRepository.save(member);

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

        BookMark bookMark1 = new BookMark();
        bookMark1.setMember(member);
        bookMark1.setPost(post1);
        bookMarkRepository.save(bookMark1);

        BookMark bookMark2 = new BookMark();
        bookMark2.setMember(member);
        bookMark2.setPost(post1);
        bookMarkRepository.save(bookMark2);

        BookMark bookMark3 = new BookMark();
        bookMark3.setMember(member);
        bookMark3.setPost(post2);
        bookMarkRepository.save(bookMark3);

        // When
        List<Post> posts = postService.getPostsOrderByBookmarksDesc();

        // Then
        assertNotNull(posts);
        assertEquals(20, posts.size());
        assertEquals("Post 1", posts.get(0).getTitle());
        assertEquals("Post 2", posts.get(1).getTitle());
    }
}
