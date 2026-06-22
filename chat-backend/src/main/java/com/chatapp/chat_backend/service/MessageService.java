package com.chatapp.chat_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chatapp.chat_backend.dto.message.SendMessageRequest;
import com.chatapp.chat_backend.entity.Conversation;
import com.chatapp.chat_backend.entity.Message;
import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.enums.MessageType;
import com.chatapp.chat_backend.repository.ConversationRepository;
import com.chatapp.chat_backend.repository.MessageRepository;
import com.chatapp.chat_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

        public Message sendMessage(Long senderId, SendMessageRequest request) {

                User sender = userRepository.findById(senderId)
                        .orElseThrow(() -> new RuntimeException("Sender not found"));

                Conversation conversation = conversationRepository.findById(request.getConversationId())
                        .orElseThrow(() -> new RuntimeException("Conversation not found"));

                Message message = Message.builder()
                        .conversation(conversation)
                        .sender(sender)
                        .content(request.getContent())
                        .messageType(MessageType.TEXT)
                        .build();

                return messageRepository.save(message);
        }

        public List<Message> getMessages(Long conversationId) {
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId);
        }
}