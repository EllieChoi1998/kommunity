package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.comment.CommentLikeOrHateService;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
// 수정 필요 (매핑 어떻게 해야할지)
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;
    private final CommentLikeOrHateService commentLikeOrHateService;

    // 댓글 추가
    @PostMapping("/create")
    public String createComment(@RequestParam Long postId, @RequestParam Integer memberId, @RequestParam String content) {
        commentService.createComment(postId, memberId, content);
        return "redirect:/post/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        PostComment comment = commentService.getCommentById(id);
        commentService.deleteComment(id);
        return "redirect:/post/" + comment.getPost().getId();
    }

    // 댓글 수정 폼
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        PostComment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "update-comment"; // update-comment.html로 이동
    }

    // 댓글 수정
    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable Long id, @RequestParam String content) {
        PostComment comment = commentService.updateComment(id, content);
        return "redirect:/post/" + comment.getPost().getId();
    }

    // 댓글 리스트 (최신순)
    @GetMapping("/list/newest")
    public String listCommentsByNewest(Model model) {
        List<PostComment> comments = commentService.getAllCommentsByNewest();
        model.addAttribute("comments", comments);
        return "comments"; // comments.html로 이동
    }

    // 댓글 리스트 (오래된순)
    @GetMapping("/list/oldest")
    public String listCommentsByOldest(Model model) {
        List<PostComment> comments = commentService.getAllCommentsByOldest();
        model.addAttribute("comments", comments);
        return "comments"; // comments.html로 이동
    }

    // 댓글 좋아요
    @PostMapping("/like/{id}")
    public String likeComment(@PathVariable("id") Long id, Principal principal) {
        Member member = memberRepository.findByName(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member name: " + principal.getName()));
        PostComment comment = commentService.getCommentById(id);
        commentLikeOrHateService.likePostComment(comment, member);
        return "redirect:/post/" + comment.getPost().getId();
    }

    // 댓글 싫어요
    @PostMapping("/dislike/{id}")
    public String dislikeComment(@PathVariable("id") Long id, Principal principal) {
        Member member = memberRepository.findByName(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member name: " + principal.getName()));
        PostComment comment = commentService.getCommentById(id);
        commentLikeOrHateService.hatePostComment(comment, member);
        return "redirect:/post/" + comment.getPost().getId();
    }
}
