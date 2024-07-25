package com.kosa.kmt.hashtag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
