package com.kosa.kmt.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "postcomment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_Id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "comment_datetime", nullable = false)
    private LocalDateTime commentDateTime;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentHate> hates;

//    @Column(name = "ano_number", nullable = false)
//    private Long anonnumber;
}
