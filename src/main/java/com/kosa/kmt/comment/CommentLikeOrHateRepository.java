package com.kosa.kmt.comment;

public interface CommentLikeOrHateRepository {
    long countLikesByCommentId(Long commentId);
    long countHatesByCommentId(Long commentId);
    void addLike(Long commentId, Long memberId);
    void addHate(Long commentId, Long memberId);
    void removeLike(Long commentId, Long memberId);
    void removeHate(Long commentId, Long memberId);
}
