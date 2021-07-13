package com.roman.tipear.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SocketListener {

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @EventListener
    public void handleDisconnect(final SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long senderId = (Long) headerAccessor.getSessionAttributes().get("senderId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        RaceMessage rm = new RaceMessage(MessageType.DISCONNECT, "", "", senderId);
        String url = "/race/topic/".concat(roomId);
        sendingOperations.convertAndSend(url, rm);
    }
}
