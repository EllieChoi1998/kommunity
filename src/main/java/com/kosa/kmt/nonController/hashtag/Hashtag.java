package com.kosa.kmt.nonController.hashtag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @OneToMany(mappedBy = "hashtag", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostHashtag> hashtags;

}
