package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostRepository;
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

    @Test
    public void testCreateMultipleComments() {
        // 생성된 PostId에 맞게 수정해야 함 (다중 생성 Test 코드)
        Long[] postIds = {21L, 22L};
        // 생성된 멤버에 맞게 memberId도 수정해야 함
        Integer memberId = 1;
        String[] comments = {
                "This is the first test comment for post 21",
                "This is the second test comment for post 21",
                "This is the third test comment for post 21",
                "This is the first test comment for post 22",
                "This is the second test comment for post 22"
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
