package com.kosa.kmt.nonController.comment;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDetailsDTO {
    private PostComment comment;
    private boolean isLikedByCurrentUser;
    private boolean isDislikedByCurrentUser;
}
