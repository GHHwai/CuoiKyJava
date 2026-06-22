package com.chatapp.chat_backend.dto.message;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Long conversationId;
    private String content;
}