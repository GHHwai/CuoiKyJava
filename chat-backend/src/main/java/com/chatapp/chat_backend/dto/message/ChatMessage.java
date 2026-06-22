package com.chatapp.chat_backend.dto.message;

import lombok.Data;

@Data
public class ChatMessage {
    private Long conversationId;
    private String content;
    private Long senderId; 
}