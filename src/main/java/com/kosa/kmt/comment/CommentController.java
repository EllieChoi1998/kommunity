package com.kosa.kmt.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeOrHateService commentLikeOrHateService;

    @PostMapping("/create/{postId}")
    public PostComment addComment(@PathVariable Long postId, @RequestParam Long memberId, @RequestParam String content) {
        return commentService.createComment(postId, memberId, content);
    }

    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    // 나중에 GET POST 형식으로 바꿀 예정
    @PutMapping("/update/{commentId}")
    public PostComment updateComment(@PathVariable Long commentId, @RequestParam String newContent) {
        return commentService.updateComment(commentId, newContent);
    }

    // 기본 GET 요청 처리 (정렬 없이 모든 댓글 조회)
    @GetMapping
    public List<PostComment> getAllComments() {
        return commentService.getAllComments();
    }

    // 댓글을 최신 순으로 조회
    @GetMapping("/newest")
    public List<PostComment> getAllCommentsNewest() {
        return commentService.getAllCommentsByNewest();
    }

    // 댓글을 오래된 순으로 조회
    @GetMapping("/oldest")
    public List<PostComment> getAllCommentsOldest() {
        return commentService.getAllCommentsByOldest();
    }

}