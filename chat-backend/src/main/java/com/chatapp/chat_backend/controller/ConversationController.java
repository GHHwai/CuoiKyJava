package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.chat_backend.dto.conversation.CreateConversationRequest;
import com.chatapp.chat_backend.entity.Conversation;
import com.chatapp.chat_backend.service.ConversationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public Conversation create(
            Authentication authentication,
            @RequestBody CreateConversationRequest request
    ) {
        Long senderId = (Long) authentication.getPrincipal();
        return conversationService.createPrivateConversation(senderId, request);
    }

    @GetMapping
    public List<Conversation> getMyConversations(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return conversationService.getMyConversations(userId);
    }
}