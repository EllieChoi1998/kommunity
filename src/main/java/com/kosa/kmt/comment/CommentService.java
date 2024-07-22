package com.kosa.kmt.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostCommentRepository postcommentRepository;

    public List<PostComment> getAllComments() {
        return postcommentRepository.findAll();
    }

    // 댓글 추가
    public PostComment createComment(Long postId, Long memberId, String content) {
        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setMemberId(memberId);
        comment.setCommentContent(content);
        comment.setCommentDateTime(LocalDateTime.now());
        return postcommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        postcommentRepository.deleteById(commentId);
    }

    // 댓글 수정
    public PostComment updateComment(Long commentId, String newContent) {
        PostComment comment = postcommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        comment.setCommentContent(newContent);

        // 수정 시 현재 시간으로 업데이트
        comment.setCommentDateTime(LocalDateTime.now());
        return postcommentRepository.save(comment);
    }

    // 최신 순 정렬
    public List<PostComment> getAllCommentsByNewest() {
        return postcommentRepository.findAllByOrderByCommentDateTimeDesc();
    }

    // 오래된 순 정렬
    public List<PostComment> getAllCommentsByOldest() {
        return postcommentRepository.findAllByOrderByCommentDateTimeAsc();
    }
}