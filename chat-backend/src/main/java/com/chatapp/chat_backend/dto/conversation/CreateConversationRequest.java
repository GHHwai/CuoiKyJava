package com.chatapp.chat_backend.dto.conversation;

import lombok.Data;

@Data
public class CreateConversationRequest {
    private Long receiverId;
}