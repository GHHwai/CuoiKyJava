package com.chatapp.chat_backend.dto.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationResponse {
    private Long id;
    private String name;
}