package com.kosa.kmt.nonController.chat;

import java.util.List;

public interface ChatService {

    Long saveChat(Chat chat, Integer memberId) throws Exception;

    List<Chat> findAllChats();
}
