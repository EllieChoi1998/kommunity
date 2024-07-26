package com.kosa.kmt.non_controller.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false)
    private Long boardId;

    @Column(name = "NAME", nullable = false)
    private String name;
}
