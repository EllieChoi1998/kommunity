package com.kosa.kmt.nonController.post;

import lombok.Data;

@Data
public class PostForm {
    private String title;
    private String content;
    private Integer memberId;
    private Integer categoryId;
    private String strHashtag;
}
