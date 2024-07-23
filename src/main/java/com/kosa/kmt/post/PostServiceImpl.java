package com.kosa.kmt.post;

import com.kosa.kmt.hashtag.HashtagService;
import com.kosa.kmt.hashtag.PostHashtagService;
import com.kosa.kmt.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostHashtagService postHashtagService;

    @Override
    public List<Post> getPostsAll() throws SQLException {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) throws SQLException {
        return postRepository.findById(id).get();
    }

    @Override
    public Long createPost(String title, String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setMemberId(memberId);
        post.setCategoryId(categoryId);

        Long postId = postRepository.save(post).getId();

        postHashtagService.setHashtag(post, strHashtag);

        return postId;
    }

    @Override
    public Long createPostNonTitle(String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException {

        Post post = new Post();
        post.setContent(content);
        post.setMemberId(memberId);
        post.setCategoryId(categoryId);

        Long postId = postRepository.save(post).getId();

        postHashtagService.setHashtag(post, strHashtag);

        return postId;
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

    @Override
    public List<Post> getPostsOrderByPostDateDesc(Post post) throws SQLException {
        return postRepository.findAllByOrderByPostDateDesc();
    }

    @Override
    public List<Post> getPostsOrderByPostDateAsc(Post post) throws SQLException {
        return postRepository.findAllByOrderByPostDateAsc();
    }
}
