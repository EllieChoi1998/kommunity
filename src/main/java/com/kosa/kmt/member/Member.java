package com.kosa.kmt.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
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


    @Builder
    public Member(String name, String email) {
        this.name = name;
        this.authEmail = email;
    }

    public Member() {

    }
}
