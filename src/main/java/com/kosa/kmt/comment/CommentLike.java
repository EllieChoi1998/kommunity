package com.kosa.kmt.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "commentlike")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment postComment;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}
