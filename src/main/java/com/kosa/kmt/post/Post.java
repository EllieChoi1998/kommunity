package com.kosa.kmt.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(length = 50)
    private String title;

    @Lob
    @Column(name = "POST_CONTENT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime postDate;

    @PrePersist
    protected void onCreate() {
        postDate = LocalDateTime.now();
    }
}