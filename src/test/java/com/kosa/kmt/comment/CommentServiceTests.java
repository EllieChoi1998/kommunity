package com.kosa.kmt.comment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentServiceTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostCommentRepository postcommentRepository;

    // 테스트 시작 전 초기화
//    @BeforeEach
//    public void setUp() {
//        postcommentRepository.deleteAll(); // Clear the repository before each test
//    }

    @Test
    public void testAddComment() {
        Long postId = 1L;
        Long memberId = 2L;
        String content = "하이요";

        // 댓글 추가
        PostComment comment = commentService.createComment(postId, memberId, content);

        // 댓글이 제대로 추가되었는지 확인
        assertNotNull(comment.getComment_Id());
        assertEquals(postId, comment.getPostId());
        assertEquals(memberId, comment.getMemberId());
        assertEquals(content, comment.getCommentContent());

        PostComment savedComment = postcommentRepository.findById(comment.getComment_Id()).orElse(null);
        assertNotNull(savedComment);
        assertEquals(comment.getComment_Id(), savedComment.getComment_Id());
    }

    @Test
    public void testDeleteComment() {
        Long postId = 1L;
        Long memberId = 2L;
        String content = "하이요";

        // 댓글 추가
        PostComment comment = commentService.createComment(postId, memberId, content);

        // 댓글이 추가된 상태인지 확인
        assertNotNull(postcommentRepository.findById(comment.getComment_Id()).orElse(null), "삭제 전 댓글이 존재해야 합니다.");

        // 댓글 삭제
        commentService.deleteComment(comment.getComment_Id());

        // 삭제 후 댓글이 데이터베이스에서 삭제되었는지 확인
        PostComment deletedComment = postcommentRepository.findById(comment.getComment_Id()).orElse(null);
        assertNull(deletedComment, "삭제해야 함");

        // 전체 댓글 수 확인
        List<PostComment> comments = commentService.getAllComments();
        assertEquals(23, comments.size(), "삭제 후 총 댓글 수는 " + comments.size() + "개여야 합니다.");
    }

    @Test
    public void testUpdateComment() {
        Long postId = 1L;
        Long memberId = 1L;
        String originalContent = "Original content";
        String updatedContent = "Updated content";

        // 댓글 추가
        PostComment comment = commentService.createComment(postId, memberId, originalContent);

        // 댓글 수정
        PostComment updatedComment = commentService.updateComment(comment.getComment_Id(), updatedContent);

        // 수정된 댓글 내용 확인
        assertEquals(updatedContent, updatedComment.getCommentContent(), "댓글 내용이 수정되어야 합니다.");

        // 데이터베이스에서 댓글을 검증
        PostComment savedComment = postcommentRepository.findById(updatedComment.getComment_Id()).orElse(null);
        assertNotNull(savedComment, "DB에 댓글이 존재해야 함");
        assertEquals(updatedContent, savedComment.getCommentContent(), "데이터베이스의 댓글 내용이 수정되어야 합니다.");
    }

    @Test
    public void testGetAllCommentsByNewest() {
        Long postId = 1L;
        Long memberId = 1L;
        String content1 = "첫 번째 생성 댓글";
        String content2 = "두 번째 생성 댓글";

        // 기존 댓글 수 확인
        int initialCommentCount = commentService.getAllComments().size();

        // 새로운 댓글 추가
        commentService.createComment(postId, memberId, content1);
        // 타임스탬프를 다르게 하기 위해 지연
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        commentService.createComment(postId, memberId, content2);

        // 최신 순으로 모든 댓글 조회
        List<PostComment> comments = commentService.getAllCommentsByNewest();

        // 기존 댓글 + 새로 추가된 2개 댓글 = 총 댓글 수 확인
        assertEquals(initialCommentCount + 2, comments.size());

        // 새로 추가한 댓글이 목록의 첫 번째 두 개 위치에 있는지 확인
        assertEquals(content2, comments.get(0).getCommentContent()); // 최신 댓글이 첫 번째에 위치해야 함
        assertEquals(content1, comments.get(1).getCommentContent());
    }

    @Test
    public void testGetAllCommentsByOldest() {
        Long postId = 1L;
        Long memberId = 1L;
        String content1 = "첫 번째 생성 댓글";
        String content2 = "두 번째 생성 댓글";

        // 기존 댓글 수 확인
        int initialCommentCount = commentService.getAllComments().size();

        // 새로운 댓글 추가
        commentService.createComment(postId, memberId, content1);
        // 타임스탬프를 다르게 하기 위해 지연
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        commentService.createComment(postId, memberId, content2);

        // 오래된 순으로 모든 댓글 조회
        List<PostComment> comments = commentService.getAllCommentsByOldest();

        // 기존 댓글 + 새로 추가된 2개 댓글 = 총 댓글 수 확인
        assertEquals(initialCommentCount + 2, comments.size());

        // 새로 추가한 댓글이 목록의 마지막 두 개 위치에 있는지 확인
        assertEquals(content1, comments.get(initialCommentCount).getCommentContent());
        assertEquals(content2, comments.get(initialCommentCount + 1).getCommentContent());
    }
}
