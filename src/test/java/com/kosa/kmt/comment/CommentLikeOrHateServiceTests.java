package com.kosa.kmt.comment;

import com.kosa.kmt.member.Member;
import com.kosa.kmt.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentLikeOrHateServiceTests {

    // CommentLikeOrHateRepository를 모킹합니다.
    @Mock
    private CommentLikeOrHateRepository commentLikeOrHateRepository;

    // CommentLikeOrHateService를 테스트하기 위해 모킹된 레포지토리를 주입합니다.
    @InjectMocks
    private CommentLikeOrHateService commentLikeOrHateService;

    private PostComment testComment;
    private Post testPost;
    private Member testMember;
    @BeforeEach
    void setUp() {
        // Mockito를 초기화합니다.
        MockitoAnnotations.openMocks(this);

        // 테스트용 Post 객체를 초기화합니다.
        testPost = new Post();
        testPost.setId(1L);

        // 테스트용 Member 객체를 초기화합니다.
        testMember = new Member();
        testMember.setMemberId(1);

        // 테스트용 PostComment 객체를 초기화합니다.
        testComment = new PostComment();
        testComment.setCommentId(1L);
        testComment.setPost(testPost);
        testComment.setMember(testMember);
        testComment.setCommentContent("Test comment");
        testComment.setCommentDateTime(LocalDateTime.now());

        // Repository 메서드를 모킹하여 기본 반환 값을 설정합니다.
        when(commentLikeOrHateRepository.countLikesByCommentId(testComment.getCommentId())).thenReturn(0L);
        when(commentLikeOrHateRepository.countHatesByCommentId(testComment.getCommentId())).thenReturn(0L);
    }

    @Test
    void testAddLikeToComment() {
        // 좋아요를 추가하는 동작을 모킹합니다.
        doNothing().when(commentLikeOrHateRepository).addLike(anyLong(), anyLong());
        when(commentLikeOrHateRepository.countLikesByCommentId(testComment.getCommentId())).thenReturn(1L);

        // 댓글에 좋아요를 추가합니다.
        commentLikeOrHateService.addLikeToComment(testComment.getCommentId(), 2L);

        // 좋아요 개수를 확인하고 검증합니다.
        long likeCount = commentLikeOrHateService.countLikesByCommentId(testComment.getCommentId());
        assertThat(likeCount).isEqualTo(1);

        // addLike 메서드가 호출되었는지 검증합니다.
        verify(commentLikeOrHateRepository).addLike(testComment.getCommentId(), 2L);
    }

    @Test
    void testAddHateToComment() {
        // 싫어요를 추가하는 동작을 모킹합니다.
        doNothing().when(commentLikeOrHateRepository).addHate(anyLong(), anyLong());
        when(commentLikeOrHateRepository.countHatesByCommentId(testComment.getCommentId())).thenReturn(1L);

        // 댓글에 싫어요를 추가합니다.
        commentLikeOrHateService.addHateToComment(testComment.getCommentId(), 2L);

        // 싫어요 개수를 확인하고 검증합니다.
        long hateCount = commentLikeOrHateService.countHatesByCommentId(testComment.getCommentId());
        assertThat(hateCount).isEqualTo(1);

        // addHate 메서드가 호출되었는지 검증합니다.
        verify(commentLikeOrHateRepository).addHate(testComment.getCommentId(), 2L);
    }

    @Test
    void testRemoveLikeFromComment() {
        // 좋아요를 제거하는 동작을 모킹합니다.
        doNothing().when(commentLikeOrHateRepository).removeLike(anyLong(), anyLong());
        when(commentLikeOrHateRepository.countLikesByCommentId(testComment.getCommentId())).thenReturn(0L);

        // 댓글에 좋아요를 추가한 후 제거합니다.
        commentLikeOrHateService.addLikeToComment(testComment.getCommentId(), 2L);
        commentLikeOrHateService.removeLikeFromComment(testComment.getCommentId(), 2L);

        // 좋아요 개수를 확인하고 검증합니다.
        long likeCount = commentLikeOrHateService.countLikesByCommentId(testComment.getCommentId());
        assertThat(likeCount).isEqualTo(0);

        // removeLike 메서드가 호출되었는지 검증합니다.
        verify(commentLikeOrHateRepository).removeLike(testComment.getCommentId(), 2L);
    }

    @Test
    void testRemoveHateFromComment() {
        // 싫어요를 제거하는 동작을 모킹합니다.
        doNothing().when(commentLikeOrHateRepository).removeHate(anyLong(), anyLong());
        when(commentLikeOrHateRepository.countHatesByCommentId(testComment.getCommentId())).thenReturn(0L);

        // 댓글에 싫어요를 추가한 후 제거합니다.
        commentLikeOrHateService.addHateToComment(testComment.getCommentId(), 2L);
        commentLikeOrHateService.removeHateFromComment(testComment.getCommentId(), 2L);

        // 싫어요 개수를 확인하고 검증합니다.
        long hateCount = commentLikeOrHateService.countHatesByCommentId(testComment.getCommentId());
        assertThat(hateCount).isEqualTo(0);

        // removeHate 메서드가 호출되었는지 검증합니다.
        verify(commentLikeOrHateRepository).removeHate(testComment.getCommentId(), 2L);
    }
}
