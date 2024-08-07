package com.kosa.kmt.nonController.member;

import com.kosa.kmt.nonController.chat.Chat;
import com.kosa.kmt.nonController.comment.PostComment;
import com.kosa.kmt.nonController.post.bookmark.BookMark;
import com.kosa.kmt.nonController.post.Post;
import com.kosa.kmt.nonController.post.hateOrLike.PostHate;
import com.kosa.kmt.nonController.post.hateOrLike.PostLike;
import jakarta.persistence.*;

import lombok.Builder;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @Builder
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

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
