package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.comment.CommentForm;
import com.kosa.kmt.nonController.comment.CommentService;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MainController mainController;

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
            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            return "posts/detail";
        }

        Member member = mainController.getCurrentMember();
        commentService.createComment(postId, member.getMemberId(), commentForm.getCommentContent());
        return "redirect:/posts/" + postId;
    }
}
