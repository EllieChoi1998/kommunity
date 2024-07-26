package com.kosa.kmt.comment;

import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.comment.PostCommentRepository;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CommentServiceTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    private Member testMember;
    private Post testPost;

    @BeforeEach
    public void setUp() {
        // Test 데이터 생성
        testMember = new Member();
        testMember.setName("Test");
        testMember.setEmail("test@example.com");
        testMember.setPassword("password");
        testMember = memberRepository.save(testMember);

        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setCategoryId(1);  // 예시 카테고리 ID
        testPost.setMember(testMember);
        testPost = postRepository.saveAndFlush(testPost);
    }

    @Test
    public void testCreateComment() {
        String content = "Test Comment";
        PostComment comment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);

        assertThat(comment).isNotNull();
        assertThat(comment.getCommentContent()).isEqualTo(content);
        assertThat(comment.getMember().getMemberId()).isEqualTo(testMember.getMemberId());
        assertThat(comment.getPost().getId()).isEqualTo(testPost.getId());
    }

    @Test
    public void testGetAllComments() {
        List<PostComment> comments = commentService.getAllComments();
        assertThat(comments).isNotNull();
    }

    @Test
    public void testUpdateComment() {
        String content = "Initial Comment";
        PostComment comment = commentService.createComment(13L, 6, content);

        String updatedContent = "Updated Comment";
        PostComment updatedComment = commentService.updateComment(comment.getCommentId(), updatedContent);

        assertThat(updatedComment.getCommentContent()).isEqualTo(updatedContent);
    }

    @Test
    public void testDeleteComment() {
        String content = "Comment to Delete";
        PostComment comment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);

        commentService.deleteComment(comment.getCommentId());

        Optional<PostComment> deletedComment = postCommentRepository.findById(comment.getCommentId());
        assertThat(deletedComment).isEmpty();
    }

    @Test
    public void testGetAllCommentsByNewest() {
        List<PostComment> comments = commentService.getAllCommentsByNewest();
        assertThat(comments).isNotNull();
    }

    @Test
    public void testGetAllCommentsByOldest() {
        List<PostComment> comments = commentService.getAllCommentsByOldest();
        assertThat(comments).isNotNull();
    }
}
