package com.kosa.kmt.nonController.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDetailsDTO {
    private Post post;
    private boolean isLikedByCurrentUser;
    private boolean isDislikedByCurrentUser;
    private boolean isBookmarkedByCurrentUser;
}
