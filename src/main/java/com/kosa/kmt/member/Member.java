package com.kosa.kmt.member;

import com.kosa.kmt.chat.Chat;
import com.kosa.kmt.comment.PostComment;
import com.kosa.kmt.hashtag.PostHashtag;
import com.kosa.kmt.post.BookMark;
import com.kosa.kmt.post.Post;
import com.kosa.kmt.post.PostHate;
import com.kosa.kmt.post.PostLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private Integer memberId;

    @Column(name = "NICKNAME", length = 10)
    private String nickname;

    @Column(name = "PASSWORD", length = 200)
    private String password;

    @Column(name = "LOGIN_TIME")
    private LocalDateTime loginTime;

    @Column(name = "LOGOUT_TIME")
    private LocalDateTime logoutTime;

    @Column(name = "NAME", length = 10, nullable = false)
    private String name;

    @Column(name = "EMAIL", length = 50, nullable = false)
    private String email;

    @Column(name = "EXTENDLOGIN", length = 1)
    private String extendLogin = "F";

    @Column(name = "AUTHEMAIL", length = 50)
    private String authEmail;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostLike> postLikes;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostHate> postHates;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Chat> chats;

    @OneToMany(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostComment> comments;
}
