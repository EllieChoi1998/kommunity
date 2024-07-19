package com.kosa.kmt.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.kosa.kmt.member.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ID", nullable = false)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CHAT_CONTENT", nullable = false, length = 50)
    private String chatContent;

    @Column(name = "CHAT_DATETIME", nullable = false)
    private LocalDateTime chatDateTime;
}
