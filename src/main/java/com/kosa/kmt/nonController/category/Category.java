package com.kosa.kmt.nonController.category;

import com.kosa.kmt.nonController.board.Board;
import com.kosa.kmt.nonController.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;
}
