package com.kosa.kmt.nonController.post;

import com.kosa.kmt.nonController.category.Category;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.hashtag.PostHashtag;
import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.post.bookmark.BookMark;
import com.kosa.kmt.nonController.post.hateOrLike.PostHate;
import com.kosa.kmt.nonController.post.hateOrLike.PostLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

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