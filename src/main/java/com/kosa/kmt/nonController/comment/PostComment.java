package com.kosa.kmt.nonController.comment;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.Post;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "POSTCOMMENT")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "COMMENT_CONTENT", nullable = false)
    private String commentContent;

    @Column(name = "COMMENT_DATETIME", nullable = false)
    private LocalDateTime commentDateTime;

    @Column(name = "ANON_NUMBER")
    @Nullable
    private String anonNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentHate> hates;

}