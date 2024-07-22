package com.kosa.kmt.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public List<Post> getPostsAll() throws SQLException {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) throws SQLException {
        return postRepository.findById(id).get();
    }

    @Override
    public Long createPost(String title, String content, Long memberId, Long categoryId) throws SQLException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setMemberId(memberId);
        post.setCategoryId(categoryId);

        return postRepository.save(post).getId();
    }

    @Override
    public Long createPostNonTitle(String content, Long memberId, Long categoryId) throws SQLException {
        Post post = new Post();
        post.setContent(content);
        post.setMemberId(memberId);
        post.setCategoryId(categoryId);

        return postRepository.save(post).getId();
    }

    @Override
    public Boolean updatePost(Post post, Long id) throws SQLException {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            postRepository.save(existingPost);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deletePost(Long id) throws SQLException {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            postRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
