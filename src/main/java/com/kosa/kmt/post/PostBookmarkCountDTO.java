package com.kosa.kmt.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookmarkCountDTO {
    private Post post;
    private Long bookmarkCount;
}
