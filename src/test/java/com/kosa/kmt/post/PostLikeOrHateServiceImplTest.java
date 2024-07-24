package com.kosa.kmt.post;

import com.kosa.kmt.member.Member;
import com.kosa.kmt.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostLikeOrHateServiceImplTest {

    @Autowired
    private PostLikeOrHateServiceImpl postLikeOrHateService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostHateRepository postHateRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Post post;
    private Member member;

    @BeforeEach
    public void setUp() {
        post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test post");
        post = postRepository.save(post);

        member = new Member();
        member.setName("Test");
        member.setEmail("test@example.com");
        member.setPassword("password");
        member = memberRepository.save(member);
    }

    @Test
    public void testLikePost() {
        // 기존에 좋아요가 없을 때
        postLikeOrHateService.likePost(post, member);
        assertNotNull(postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));
        assertNull(postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));

        // 기존에 좋아요가 있을 때
        postLikeOrHateService.likePost(post, member);
        assertNull(postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));
    }

    @Test
    public void testHatePost() {
        // 기존에 싫어요가 없을 때
        postLikeOrHateService.hatePost(post, member);
        assertNotNull(postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));
        assertNull(postLikeRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));

        // 기존에 싫어요가 있을 때
        postLikeOrHateService.hatePost(post, member);
        assertNull(postHateRepository.findByPost_IdAndMember_MemberId(post.getId(), member.getMemberId()));
    }
}