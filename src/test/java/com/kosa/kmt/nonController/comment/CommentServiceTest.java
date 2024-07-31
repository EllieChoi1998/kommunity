package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    private Post post;
    private Member member;

    @Test
    public void testCreateMultipleComments() {
        Long[] postIds = {13L, 14L};
        Integer memberId = 65;
        String[] comments = {
                "This is the first test comment for post 13",
                "This is the second test comment for post 13",
                "This is the third test comment for post 13",
                "This is the first test comment for post 14",
                "This is the second test comment for post 14"
        };

        // Ensure the member exists
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        assertTrue(optionalMember.isPresent(), "Member should exist");

        for (Long postId : postIds) {
            // Ensure the post exists
            Optional<Post> optionalPost = postRepository.findById(postId);
            assertTrue(optionalPost.isPresent(), "Post should exist");

            // Create comments for the post
            for (String content : comments) {
                PostComment comment = commentService.createComment(postId, memberId, content);

                // Verify the comment was created and saved
                assertNotNull(comment);
                assertNotNull(comment.getCommentId());

                // Fetch the comment from the repository
                Optional<PostComment> fetchedComment = postCommentRepository.findById(comment.getCommentId());
                assertTrue(fetchedComment.isPresent());

                // Verify the details of the fetched comment
                PostComment savedComment = fetchedComment.get();
                assertEquals(postId, savedComment.getPost().getId());
                assertEquals(memberId, savedComment.getMember().getMemberId());
                assertEquals(content, savedComment.getCommentContent());
                assertNotNull(savedComment.getCommentDateTime());
            }
        }
    }
}
