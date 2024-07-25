package com.kosa.kmt.post;

import com.kosa.kmt.comment.PostComment;
import com.kosa.kmt.hashtag.PostHashtag;
import com.kosa.kmt.member.Member;
import jakarta.persistence.*;
import lombok.*;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(name = "CATEGORY_ID")
    private Integer categoryId;

//    @Column(name = "MEMBER_ID")
//    private Integer memberId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(length = 50)
    private String title;

    @Lob
    @Column(name = "POST_CONTENT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime postDate;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostHashtag> hashtags;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostHate> hates;


    @PrePersist
    protected void onCreate() {
        postDate = LocalDateTime.now();
    }
}