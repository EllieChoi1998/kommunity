package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.board.Board;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostService {
    // find all
    public List<Post> getPostsAll() throws SQLException;

    // find by id
    public Post getPostById(Long id) throws SQLException;

    public List<Post> getPostsByCategory(Long categoryId) throws SQLException;

    public List<Post> getPostsByBoard(Long boardId) throws SQLException;

    // create normal post
    public Long createPost(String title, String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException;

    // create non-title post
    public Long createPostNonTitle(String content, Integer memberId, Integer categoryId, String strHashtag) throws SQLException;

    // update post
    public Boolean updatePost(Post post, Long id) throws SQLException;

    // delete post
    public Boolean deletePost(Long id) throws SQLException;

    // order by date desc post
    public List<Post> getPostsOrderByPostDateDesc() throws SQLException;

    // order by date asc post
    public List<Post> getPostsOrderByPostDateAsc() throws SQLException;

    // order by bookmarks count desc post
    public List<Post> getPostsOrderByBookmarksDesc() throws SQLException;

    // order by like comment count desc post
    public List<Post> getPostsOrderByCommentsDesc() throws SQLException;

    // find all post hates > likes
    public List<Post> getPostsWithMoreHatesThanLikes();


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
