//package com.kosa.kmt.nonController.comment;
//
//import com.kosa.kmt.nonController.board.Board;
//import com.kosa.kmt.nonController.board.BoardRepository;
//import com.kosa.kmt.nonController.category.Category;
//import com.kosa.kmt.nonController.category.CategoryRepository;
//import com.kosa.kmt.nonController.member.Member;
//import com.kosa.kmt.nonController.member.MemberRepository;
//import com.kosa.kmt.nonController.post.Post;
//import com.kosa.kmt.nonController.post.PostRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class CommentServiceTests {
//
//    @Autowired
//    private CommentService commentService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private BoardRepository boardRepository; // BoardRepository 추가
//
//    private Member testMember;
//    private Post testPost;
//    private Category testCategory; // Category 필드 추가
//    private Board testBoard; // Board 필드 추가
//
//    @BeforeEach
//    public void setup() {
//        // 테스트를 위한 사용자 생성
//        testMember = new Member();
//        testMember.setName("Test User");
//        testMember.setEmail("test@example.com");
//        testMember.setPassword("password");
//        memberRepository.save(testMember);
//
//        // 테스트를 위한 보드 및 카테고리 생성
//        Board testBoard = new Board();
//        testBoard.setName("Test Board");
//        boardRepository.saveBoard(testBoard);
//
//        testCategory = new Category();
//        testCategory.setName("Test Category");
//        testCategory.setBoard(testBoard);
//        categoryRepository.saveCategory(testCategory);
//
//        // 테스트를 위한 게시물 생성
//        testPost = new Post();
//        testPost.setTitle("Test Post");
//        testPost.setContent("This is a test post.");
//        testPost.setMember(testMember);
//        testPost.setCategory(testCategory);
//        postRepository.save(testPost);
//    }
//
//
//    @Test
//    public void testCreateComment() {
//        String content = "This is a test comment.";
//        PostComment comment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);
//
//        assertNotNull(comment);
//        assertEquals(content, comment.getCommentContent());
//        assertEquals(testMember.getMemberId(), comment.getMember().getMemberId());
//        assertEquals(testPost.getId(), comment.getPost().getId());
//    }
//
//    @Test
//    public void testGetCommentById() {
//        String content = "This is a test comment.";
//        PostComment createdComment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);
//
//        PostComment foundComment = commentService.getCommentById(createdComment.getCommentId());
//        assertNotNull(foundComment);
//        assertEquals(createdComment.getCommentId(), foundComment.getCommentId());
//    }
//
//    @Test
//    public void testUpdateComment() {
//        String content = "This is a test comment.";
//        PostComment createdComment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);
//
//        String newContent = "This is an updated comment.";
//        PostComment updatedComment = commentService.updateComment(createdComment.getCommentId(), newContent);
//
//        assertNotNull(updatedComment);
//        assertEquals(newContent, updatedComment.getCommentContent());
//    }
//
//    @Test
//    public void testDeleteComment() {
//        String content = "This is a test comment.";
//        PostComment createdComment = commentService.createComment(testPost.getId(), testMember.getMemberId(), content);
//
//        commentService.deleteComment(createdComment.getCommentId());
//        assertThrows(RuntimeException.class, () -> {
//            commentService.getCommentById(createdComment.getCommentId());
//        });
//    }
//
//    @Test
//    public void testGetAllCommentsByNewest() {
//        PostComment comment1 = commentService.createComment(testPost.getId(), testMember.getMemberId(), "First comment");
//        PostComment comment2 = commentService.createComment(testPost.getId(), testMember.getMemberId(), "Second comment");
//
//        List<PostComment> comments = commentService.getAllCommentsByNewest();
//        assertThat(comments).hasSize(2);
//        assertEquals("Second comment", comments.get(0).getCommentContent());
//        assertEquals("First comment", comments.get(1).getCommentContent());
//    }
//
//    @Test
//    public void testGetAllCommentsByOldest() {
//        PostComment comment1 = commentService.createComment(testPost.getId(), testMember.getMemberId(), "First comment");
//        PostComment comment2 = commentService.createComment(testPost.getId(), testMember.getMemberId(), "Second comment");
//
//        List<PostComment> comments = commentService.getAllCommentsByOldest();
//        assertThat(comments).hasSize(2);
//        assertEquals("First comment", comments.get(0).getCommentContent());
//        assertEquals("Second comment", comments.get(1).getCommentContent());
//    }
//}