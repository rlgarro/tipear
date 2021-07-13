package com.roman.tipear.controller;

import com.roman.tipear.websocket.RaceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SocketController {

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/topic/message/{room}")
    @SendTo("/race/topic/{room}")
    public RaceMessage message(@PathVariable String room, @Payload RaceMessage message) throws Exception {
        return message;
    }

    @MessageMapping("/topic/{room}/new")
    @SendTo("/race/topic/{room}")
    public RaceMessage message(@DestinationVariable String room,
                               @Payload RaceMessage message,
                               SimpMessageHeaderAccessor headerAccessor) {

        headerAccessor.getSessionAttributes().put("roomId", room);
        headerAccessor.getSessionAttributes().put("senderId", message.getSenderId());
        return message;
    }
}
