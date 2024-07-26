package com.kosa.kmt.non_controller.post;

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
