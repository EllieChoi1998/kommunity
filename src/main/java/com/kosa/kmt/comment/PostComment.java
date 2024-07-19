package com.kosa.kmt.comment;

import com.kosa.kmt.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    // 댓글 내용
    private String content;

    // 생성 시간
    private LocalDateTime createDate;

    /*
     * 게시글 한 개에 댓글 여러 개 달릴 수 있음
     * Post 1
     */
    @ManyToOne
    private Post post;

    @ManyToOne
    private Member author;

    // 수정 시간
    private LocalDateTime modifyDate;

    @ManyToMany
    Set<Member> voter;
}
