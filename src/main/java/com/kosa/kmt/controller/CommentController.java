package com.kosa.kmt.controller;

import com.kosa.kmt.non_controller.comment.CommentLikeOrHateService;
import com.kosa.kmt.non_controller.comment.CommentService;
import com.kosa.kmt.non_controller.comment.PostComment;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PostComment addComment(@PathVariable Long postId, @RequestParam Integer memberId, @RequestParam String content) {
        return commentService.createComment(postId, memberId, content);
    }

    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/update/{commentId}")
    public PostComment updateComment(@PathVariable Long commentId, @RequestParam String newContent) {
        // 기존 댓글을 가져옵니다.
        PostComment comment = commentService.getCommentById(commentId);
        // 댓글을 수정합니다.
        return commentService.updateComment(commentId, newContent);
    }

    @GetMapping
    public List<PostComment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/newest")
    public List<PostComment> getAllCommentsNewest() {
        return commentService.getAllCommentsByNewest();
    }

    @GetMapping("/oldest")
    public List<PostComment> getAllCommentsOldest() {
        return commentService.getAllCommentsByOldest();
    }
}
