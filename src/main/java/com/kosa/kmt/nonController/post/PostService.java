package com.kosa.kmt.nonController.post;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
    // find all
    public List<Post> getPostsAll() throws SQLException;

    // find by id
    public Post getPostById(Long id) throws SQLException;

    // create normal post
    public Long createPost(String title, String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException;

    // create non-title post
    public Long createPostNonTitle(String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException;

    // update post
    public Boolean updatePost(Post post, Long id) throws SQLException;

    // delete post
    public Boolean deletePost(Long id) throws SQLException;

    // order by date desc post
    public List<Post> getPostsOrderByPostDateDesc(Post post) throws SQLException;

    // order by date asc post
    public List<Post> getPostsOrderByPostDateAsc(Post post) throws SQLException;

    // order by bookmarks count desc post
    public List<Post> getPostsOrderByBookmarksDesc() throws SQLException;

    // order by like comment count desc post
    public List<Post> getPostsOrderByCommetsDesc() throws SQLException;
}
