package com.kosa.kmt.post;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
    // find all
    public List<Post> getPostsAll() throws SQLException;

    // find by id
    public Post getPostById(Long id) throws SQLException;

    // create normal post
    public void createPost(String title, String content, Long memberId, Long categoryId) throws SQLException;

    // create non-title post
    public void createPostNonTitle(String content, Long memberId, Long categoryId) throws SQLException;

    // update post
    public void updatePost(Post post, Long id, String title, String content, Long memberId, Long categoryId) throws SQLException;

    // delete post
    public void deletePost(Long id) throws SQLException;
}
