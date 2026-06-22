package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.chat_backend.dto.message.MessageResponse;
import com.chatapp.chat_backend.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping("/{conversationId}")
    public List<MessageResponse> getMessages(@PathVariable Long conversationId) {

        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getConversation().getId(),
                        m.getSender().getId(),
                        m.getContent()
                ))
                .toList();
    }
}