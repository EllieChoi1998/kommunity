package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.category.CategoryRepository;
import com.kosa.kmt.nonController.category.CategoryService;
import com.kosa.kmt.nonController.comment.PostCommentRepository;
import com.kosa.kmt.nonController.hashtag.PostHashtagService;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostHashtagService postHashtagService;
    private final MemberRepository memberRepository;
    private final BookMarkRepository bookMarkRepository;
    private final PostCommentRepository postCommentRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;


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
    public List<Post> getPostsByCategory(Long categoryId) throws SQLException {
        return postRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Post> getPostsByBoard(Long boardId) {
        List<Category> categories = categoryRepository.findByBoardId(boardId);
        List<Post> posts = new ArrayList<>();
        for (Category category : categories) {
            posts.addAll(postRepository.findByCategoryCategoryId(category.getCategoryId()));
        }
        return posts;
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

        Optional<Category> optionalCategory = categoryRepository.findById(Long.valueOf(categoryId));
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            post.setCategory(category);
        } else {
            throw new EntityNotFoundException("Category not found with id " + categoryId);
        }

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

        Optional<Category> optionalCategory = categoryRepository.findById(Long.valueOf(categoryId));
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            post.setCategory(category);
        } else {
            throw new EntityNotFoundException("Category not found with id " + categoryId);
        }

        Long postId = postRepository.save(post).getId();

        postHashtagService.setHashtag(post, strHashtag);

        return postId;
    }

    @Override
    public Boolean updatePost(Long postId, String title, String content) throws SQLException {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();

            existingPost.setTitle(title);
            existingPost.setContent(content);
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
    public List<Post> getPostsOrderByPostDateDesc() throws SQLException {
        return postRepository.findAllByOrderByPostDateDesc();
    }

    @Override
    public List<Post> getPostsOrderByPostDateAsc() throws SQLException {
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
    public List<Post> getPostsOrderByCommentsDesc() throws SQLException {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostCommentCountDTO(post, postCommentRepository.countCommentsByPost(post)))
                .sorted(Comparator.comparing(PostCommentCountDTO::getCommentCount).reversed())
                .map(PostCommentCountDTO::getPost)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsWithMoreHatesThanLikes() {
        return postRepository.findPostsWithMoreHatesThanLikes();
    }


    @Override
    public List<Post> getPostsByBoardOrderByPostDateDesc(Long boardId) throws SQLException {
        return postRepository.findAllByCategory_Board_BoardIdOrderByPostDateDesc(boardId);
    }

    @Override
    public List<Post> getPostsByBoardOrderByPostDateAsc(Long boardId) throws SQLException {
        return postRepository.findAllByCategory_Board_BoardIdOrderByPostDateAsc(boardId);
    }

    @Override
    public List<Post> getPostsByBoardOrderByBookmarksDesc(Long boardId) throws SQLException {
        List<Post> posts = postRepository.findAllByCategory_Board_BoardId(boardId);
        return posts.stream()
                .map(post -> new PostBookmarkCountDTO(post, bookMarkRepository.countBookMarksByPost(post)))
                .sorted(Comparator.comparing(PostBookmarkCountDTO::getBookmarkCount).reversed())
                .map(PostBookmarkCountDTO::getPost)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByBoardOrderByCommentsDesc(Long boardId) throws SQLException {
        List<Post> posts = postRepository.findAllByCategory_Board_BoardId(boardId);
        return posts.stream()
                .map(post -> new PostCommentCountDTO(post, postCommentRepository.countCommentsByPost(post)))
                .sorted(Comparator.comparing(PostCommentCountDTO::getCommentCount).reversed())
                .map(PostCommentCountDTO::getPost)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByCategoryOrderByPostDateDesc(Long categoryId) throws SQLException {
        return postRepository.findAllByCategory_CategoryIdOrderByPostDateDesc(categoryId);
    }

    @Override
    public List<Post> getPostsByCategoryOrderByPostDateAsc(Long categoryId) throws SQLException {
        return postRepository.findAllByCategory_CategoryIdOrderByPostDateAsc(categoryId);
    }

    @Override
    public List<Post> getPostsByCategoryOrderByBookmarksDesc(Long categoryId) throws SQLException {
        List<Post> posts = postRepository.findAllByCategory_CategoryId(categoryId);
        return posts.stream()
                .map(post -> new PostBookmarkCountDTO(post, bookMarkRepository.countBookMarksByPost(post)))
                .sorted(Comparator.comparing(PostBookmarkCountDTO::getBookmarkCount).reversed())
                .map(PostBookmarkCountDTO::getPost)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByCategoryOrderByCommentsDesc(Long categoryId) throws SQLException {
        List<Post> posts = postRepository.findAllByCategory_CategoryId(categoryId);
        return posts.stream()
                .map(post -> new PostCommentCountDTO(post, postCommentRepository.countCommentsByPost(post)))
                .sorted(Comparator.comparing(PostCommentCountDTO::getCommentCount).reversed())
                .map(PostCommentCountDTO::getPost)
                .collect(Collectors.toList());
    }
}
