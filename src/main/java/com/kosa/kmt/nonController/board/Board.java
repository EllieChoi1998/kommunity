package com.kosa.kmt.nonController.board;

import com.kosa.kmt.nonController.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Category> categories;
}
