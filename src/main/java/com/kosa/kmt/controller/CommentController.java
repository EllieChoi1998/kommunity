package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.board.BoardService;
import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentLikeOrHateService;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final BoardService boardService;
    private final MainController mainController;
    private final CommentLikeOrHateService commentLikeOrHateService;

    @PostMapping("/create/{postId}")
    @PreAuthorize("isAuthenticated()")
    public String createComment(@PathVariable Long postId,
                                @Valid @ModelAttribute CommentForm commentForm,
                                BindingResult bindingResult,
                                Principal principal,
                                Model model) throws SQLException {

        if (bindingResult.hasErrors()) {
            Post post = postService.getPostById(postId);
            List<PostComment> comments = commentService.getCommentsByPostId(postId);

            Long boardId = post.getCategory().getBoard().getBoardId();
            mainController.addCommonAttributes(model, boardId);

            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("selectedBoardId", boardId);
            return "posts/detail";
        }

        Member member = mainController.getCurrentMember();
        commentService.createComment(postId, member.getMemberId(), commentForm.getCommentContent());

        Long boardId = postService.getPostById(postId).getCategory().getBoard().getBoardId();
        model.addAttribute("selectedBoardId", boardId);

        return "redirect:/posts/" + postId;
    }

    // 댓글 수정 폼을 보여주는 메서드
    @GetMapping("/update/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String showUpdateForm(@PathVariable Long commentId, Model model) {
        PostComment comment = commentService.getCommentById(commentId);
        Member member = mainController.getCurrentMember();
        if (comment.getMember().getMemberId().equals(member.getMemberId())) {
            CommentForm commentForm = new CommentForm();
            commentForm.setCommentContent(comment.getCommentContent());
            model.addAttribute("commentForm", commentForm);

            // Add boards and selectedBoardId to the model
            List<Board> boards = boardService.findAllBoards();
            Long selectedBoardId = comment.getPost().getCategory().getBoard().getBoardId();
            model.addAttribute("boards", boards);
            model.addAttribute("selectedBoardId", selectedBoardId);

            return "comment/commentform";
        }
        return "redirect:/posts/" + comment.getPost().getId(); // 권한이 없으면 원래 페이지로 리디렉트
    }

    // 댓글 수정 요청을 처리하는 메서드
    @PostMapping("/update/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String updateComment(@PathVariable Long commentId,
                                @Valid @ModelAttribute CommentForm commentForm,
                                BindingResult bindingResult,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment/commentform";
        }

        Member member = mainController.getCurrentMember();
        PostComment comment = commentService.getCommentById(commentId);
        if (comment.getMember().getMemberId().equals(member.getMemberId())) {
            commentService.updateComment(commentId, commentForm.getCommentContent());
        }
        return "redirect:/posts/" + comment.getPost().getId(); // 수정 후 원래 페이지로 리디렉트
    }

    // 삭제 기능
    @PostMapping("/delete/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Member member = mainController.getCurrentMember();
        PostComment comment = commentService.getCommentById(commentId);
        if (comment.getMember().getMemberId().equals(member.getMemberId())) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // 좋아요 기능
    @PostMapping("/like/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId) {
        PostComment comment = commentService.getCommentById(commentId);
        Member member = mainController.getCurrentMember();
        commentLikeOrHateService.likePostComment(comment, member);
        return ResponseEntity.ok().build();
    }

    // 싫어요 기능
    @PostMapping("/dislike/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> dislikeComment(@PathVariable Long commentId) {
        PostComment comment = commentService.getCommentById(commentId);
        Member member = mainController.getCurrentMember();
        commentLikeOrHateService.hatePostComment(comment, member);
        return ResponseEntity.ok().build();
    }

    // 댓글 정렬 기능 추가
    @GetMapping("/sorted")
    public String getSortedComments(@RequestParam Long postId,
                                    @RequestParam String order,
                                    Model model) throws SQLException {
        List<PostComment> comments;
        if ("newest".equals(order)) {
            comments = commentService.getAllCommentsByNewest();
        } else {
            comments = commentService.getAllCommentsByOldest();
        }

        Post post = postService.getPostById(postId);
        Member member = mainController.getCurrentMember();

        mainController.addCommonAttributes(model, post.getCategory().getBoard().getBoardId());

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("member", member);
        model.addAttribute("commentForm", new CommentForm());
        return "posts/detail";
    }
}
