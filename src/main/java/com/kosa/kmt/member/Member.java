package com.kosa.kmt.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId", nullable = false)
    private Integer memberId;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "loginTime")
    private LocalDateTime loginTime;

    @Column(name = "logoutTime")
    private LocalDateTime logoutTime;

    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "extendLogin", length = 1)
    private String extendLogin = "F";

    @Column(name = "authEmail", length = 50)
    private String authEmail;



}
