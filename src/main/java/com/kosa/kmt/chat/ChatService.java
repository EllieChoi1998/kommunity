package com.kosa.kmt.chat;

import com.kosa.kmt.member.Member;

import java.util.List;

public interface ChatService {

    Chat saveChat(Chat chat, Integer memberId) throws Exception;

    List<Chat> findAllChats();
}
