package com.kosa.kmt.nonController.post.bookmark;

import com.kosa.kmt.nonController.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookmarkCountDTO {
    private Post post;
    private Long bookmarkCount;
}
