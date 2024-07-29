package com.kosa.kmt.nonController.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentCountDTO {
    private Post post;
    private Long commentCount;
}
