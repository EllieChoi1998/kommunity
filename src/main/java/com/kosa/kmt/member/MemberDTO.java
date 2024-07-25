package com.kosa.kmt.member;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class MemberDTO implements Serializable {
    private String name;
    private String email;
    private String authEmail;
    private String nickname;
    private String password;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String extendLogin = "F";

    public MemberDTO(Member userEntity) {
        this.name = userEntity.getName();
        this.email = userEntity.getEmail();
        this.authEmail = userEntity.getAuthEmail();
    }
}