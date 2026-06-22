package com.chatapp.chat_backend.dto.frienship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRequest {
    private Long requestId;
    private Long senderId;
    private String senderName;
}