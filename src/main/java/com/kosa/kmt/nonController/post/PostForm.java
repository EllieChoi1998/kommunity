package com.kosa.kmt.nonController.post;

import lombok.Data;

@Data
public class PostForm {
    private String title;
    private String content;
    private Integer memberId;
    private String nickname;
    private Integer categoryId;
    private Integer boardId;
    private String strHashtag;
}
