package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.hashtag.PostHashtagService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostHashtagService postHashtagService;
    private final MemberRepository memberRepository;
    private final BookMarkRepository bookMarkRepository;

    @Override
    public List<Post> getPostsAll() throws SQLException {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) throws SQLException {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new SQLException("No post found with id: " + id);
        }
    }

    @Override
    public Long createPost(String title, String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            post.setMember(member);
        } else {
            throw new EntityNotFoundException("Member not found with id " + memberId);
        }

        post.setCategoryId(categoryId);

        Long postId = postRepository.save(post).getId();

        postHashtagService.setHashtag(post, strHashtag);

        return postId;
    }

    @Override
    public Long createPostNonTitle(String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException {

        Post post = new Post();
        post.setContent(content);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            post.setMember(member);
        } else {
            throw new EntityNotFoundException("Member not found with id " + memberId);
        }

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

    @Override
    public List<Post> getPostsOrderByBookmarksDesc() throws SQLException {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostBookmarkCountDTO(post, bookMarkRepository.countBookMarksByPost(post)))
                .sorted(Comparator.comparing(PostBookmarkCountDTO::getBookmarkCount).reversed())
                .map(PostBookmarkCountDTO::getPost)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsOrderByCommetsDesc() throws SQLException {
        return List.of();
    }


}
