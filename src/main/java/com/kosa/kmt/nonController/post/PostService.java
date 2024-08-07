package com.kosa.kmt.nonController.post;

import java.sql.SQLException;
import java.util.List;

public interface PostService {
    // find all
    public List<Post> getPostsAll() throws SQLException;

    // find by id
    public Post getPostById(Long id) throws SQLException;

    public List<Post> getPostsByCategory(Long categoryId) throws SQLException;

    // create post
    public Long createPost(String title, String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException;
    
    // update post
    Boolean updatePost(Long postId, String title, String content, Integer categoryId, String strHashtag) throws SQLException;

    // delete post
    public Boolean deletePost(Long id) throws SQLException;

    // order by bookmarks count desc post
    public List<Post> getPostsOrderByBookmarksDesc() throws SQLException;

    // order by like comment count desc post
    public List<Post> getPostsOrderByCommentsDesc() throws SQLException;

    // find all post hates > likes
    // 확장 가능
//    public List<Post> getPostsWithMoreHatesThanLikes();


    List<Post> getPostsByBoardOrderByPostDateDesc(Long boardId) throws SQLException;
    List<Post> getPostsByBoardOrderByPostDateAsc(Long boardId) throws SQLException;
    List<Post> getPostsByBoardOrderByBookmarksDesc(Long boardId) throws SQLException;
    List<Post> getPostsByBoardOrderByCommentsDesc(Long boardId) throws SQLException;

    List<Post> getPostsByCategoryOrderByPostDateDesc(Long categoryId) throws SQLException;
    List<Post> getPostsByCategoryOrderByPostDateAsc(Long categoryId) throws SQLException;
    List<Post> getPostsByCategoryOrderByBookmarksDesc(Long categoryId) throws SQLException;
    List<Post> getPostsByCategoryOrderByCommentsDesc(Long categoryId) throws SQLException;

    List<Post> findPostsByAnyHashtags(Long boardId, List<String> hashtags) throws SQLException;
}
