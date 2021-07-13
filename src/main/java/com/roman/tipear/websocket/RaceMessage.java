package com.roman.tipear.websocket;

public class RaceMessage {
    private MessageType type;
    private String sender;
    private String content;
    private Long senderId;

    public RaceMessage() {
    }

    public RaceMessage(MessageType type, String sender, String content, Long senderId) {
        this.type = type;
        this.sender = sender;
        this.content = content;
        this.senderId = senderId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
