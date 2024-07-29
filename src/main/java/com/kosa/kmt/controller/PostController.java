package com.kosa.kmt.controller;

import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.PostForm;
import com.kosa.kmt.nonController.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getAllPosts(Model model) throws SQLException {
        List<Post> posts = postService.getPostsAll();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) throws SQLException {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/new")
    public String createPostForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "posts/create";
    }

    @PostMapping
    public String createPost(@ModelAttribute PostForm postForm) throws SQLException {
        postService.createPost(postForm.getTitle(), postForm.getContent(), postForm.getMemberId(), postForm.getCategoryId(), postForm.getStrHashtag());
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) throws SQLException {
        Post post = postService.getPostById(id);
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute PostForm postForm) throws SQLException {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        postService.updatePost(post, id);
        return "redirect:/posts";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) throws SQLException {
        postService.deletePost(id);
        return "redirect:/posts";
    }

}
