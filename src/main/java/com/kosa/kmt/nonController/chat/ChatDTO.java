package com.kosa.kmt.nonController.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatDTO {
    private String chatContent;
    private LocalDateTime chatDateTime;
    private Integer chatWriterId;
    private String chatWriterName; // 추가: 보낸 유저의 이름
    private boolean isCurrentUser; // 현재 사용자인지 여부

    public ChatDTO(String chatContent, LocalDateTime chatDateTime, Integer chatWriterId, String chatWriterName, boolean isCurrentUser) {
        this.chatContent = chatContent;
        this.chatDateTime = chatDateTime;
        this.chatWriterId = chatWriterId;
        this.chatWriterName = chatWriterName;
        this.isCurrentUser = isCurrentUser;
    }

    public boolean getIsCurrentUser() {
        return isCurrentUser;
    }

}



