package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<PostComment> getAllComments() {
        return postCommentRepository.findAll();
    }

    // 댓글 조회
    public PostComment getCommentById(Long commentId) {
        return postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
    }

    public List<PostComment> getCommentsByPostId(Long postId) {
        return postCommentRepository.findByPostId(postId);
    }

    // 댓글 추가
    public PostComment createComment(Long postId, Integer memberId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id " + memberId));

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setCommentContent(content);
        comment.setCommentDateTime(LocalDateTime.now());

        return postCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        postCommentRepository.delete(comment);
    }

    // 댓글 수정
    public PostComment updateComment(Long commentId, String newContent) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        comment.setCommentContent(newContent);

        // 수정 시 현재 시간으로 업데이트
        comment.setCommentDateTime(LocalDateTime.now());
        return postCommentRepository.save(comment);
    }

    // 최신 순 정렬
    public List<PostComment> getAllCommentsByNewest() {
        return postCommentRepository.findAllByOrderByCommentDateTimeDesc();
    }

    // 오래된 순 정렬
    public List<PostComment> getAllCommentsByOldest() {
        return postCommentRepository.findAllByOrderByCommentDateTimeAsc();
    }
}