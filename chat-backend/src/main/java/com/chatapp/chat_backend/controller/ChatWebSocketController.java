package com.chatapp.chat_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.chatapp.chat_backend.dto.message.SendMessageRequest;
import com.chatapp.chat_backend.entity.Conversation;
import com.chatapp.chat_backend.entity.Message;
import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.repository.ConversationRepository;
import com.chatapp.chat_backend.repository.MessageRepository;
import com.chatapp.chat_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(SendMessageRequest request, Authentication auth) {

        Long userId = (Long) auth.getPrincipal();

        User sender = userRepository.findById(userId).orElseThrow();
        Conversation conversation = conversationRepository.findById(request.getConversationId()).orElseThrow();

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.getContent())
                .build();

        message = messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + conversation.getId(),
                message
        );
    }
}