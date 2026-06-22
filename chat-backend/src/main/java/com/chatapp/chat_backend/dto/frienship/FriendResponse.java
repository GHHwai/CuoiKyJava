package com.chatapp.chat_backend.dto.frienship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendResponse {

    private Long userId;
    private String username;
    private String email;
}
