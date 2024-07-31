package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.hashtag.Hashtag;
import com.kosa.kmt.nonController.hashtag.PostHashtag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostForm {
    private Long id;
    private String title;
    private String content;
    private String renderedContent;
    private Integer memberId;
    private String nickname;
    private Integer categoryId;
    private Integer boardId;
    private String strHashtag;
    private LocalDateTime postDate;
    private List<PostHashtag> hashtags;
}
