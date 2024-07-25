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

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, @RequestParam Long memberId) {
        if (commentLikeOrHateService.countLikesByCommentId(commentId) == 0 &&
                commentLikeOrHateService.countHatesByCommentId(commentId) == 0) {
            commentLikeOrHateService.addLikeToComment(commentId, memberId);
        } else if (commentLikeOrHateService.countHatesByCommentId(commentId) > 0) {
            commentLikeOrHateService.removeHateFromComment(commentId, memberId);
            commentLikeOrHateService.addLikeToComment(commentId, memberId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/hate")
    public ResponseEntity<Void> hateComment(@PathVariable Long commentId, @RequestParam Long memberId) {
        if (commentLikeOrHateService.countLikesByCommentId(commentId) == 0 &&
                commentLikeOrHateService.countHatesByCommentId(commentId) == 0) {
            commentLikeOrHateService.addHateToComment(commentId, memberId);
        } else if (commentLikeOrHateService.countLikesByCommentId(commentId) > 0) {
            commentLikeOrHateService.removeLikeFromComment(commentId, memberId);
            commentLikeOrHateService.addHateToComment(commentId, memberId);
        }
        return ResponseEntity.ok().build();
    }
}
