package com.kosa.kmt.nonController.chat;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import java.util.ArrayList;
import java.util.List;

@Log
public class ChattingHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("#ChattingHandler, afterConnectionEstablished");
        sessionList.add(session);

        System.out.println(session.getPrincipal().getName() + "님이 입장하셨습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.println("#ChattingHandler, handleMessage");
        System.out.println(session.getId() + ": " + message);

        for(WebSocketSession s : sessionList) {
            s.sendMessage(new TextMessage(session.getPrincipal().getName() + ":" + message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println("#ChattingHandler, afterConnectionClosed");

        sessionList.remove(session);

        System.out.println(session.getPrincipal().getName() + "님이 퇴장하셨습니다.");
    }
}