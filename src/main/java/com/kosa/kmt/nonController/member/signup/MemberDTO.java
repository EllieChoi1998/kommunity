package com.kosa.kmt.nonController.member.signup;

import com.kosa.kmt.nonController.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public MemberDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
}