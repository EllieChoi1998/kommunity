package com.kosa.kmt.chat;

import java.util.List;

public interface ChatRepository {
    Chat save(Chat chat) throws Exception;
    List<Chat> findAll();

    void executeCleanup();
}
