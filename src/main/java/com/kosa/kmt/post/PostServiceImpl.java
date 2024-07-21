package com.kosa.kmt.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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
    public void createPost(String title, String content, Long memberId, Long categoryId) throws SQLException {
        Post post = new Post();

    }

    @Override
    public void createPostNonTitle(String content, Long memberId, Long categoryId) throws SQLException {

    }

    @Override
    public void updatePost(Post post, Long id, String title, String content, Long memberId, Long categoryId) throws SQLException {

    }
}
