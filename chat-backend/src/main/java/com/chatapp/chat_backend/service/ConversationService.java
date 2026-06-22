package com.chatapp.chat_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chatapp.chat_backend.dto.conversation.CreateConversationRequest;
import com.chatapp.chat_backend.entity.Conversation;
import com.chatapp.chat_backend.entity.ConversationMember;
import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.enums.ConversationType;
import com.chatapp.chat_backend.repository.ConversationMemberRepository;
import com.chatapp.chat_backend.repository.ConversationRepository;
import com.chatapp.chat_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository memberRepository;
    private final UserRepository userRepository;

    public Conversation createPrivateConversation(Long senderId, CreateConversationRequest request) {

        if (senderId.equals(request.getReceiverId())) {
            throw new RuntimeException("Cannot chat with yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // TODO (nâng cấp sau): check đã tồn tại conversation chưa

        Conversation conversation = Conversation.builder()
                .type(ConversationType.PRIVATE)
                .build();

        conversation = conversationRepository.save(conversation);

        ConversationMember m1 = ConversationMember.builder()
                .conversation(conversation)
                .user(sender)
                .role("MEMBER")
                .build();

        ConversationMember m2 = ConversationMember.builder()
                .conversation(conversation)
                .user(receiver)
                .role("MEMBER")
                .build();

        memberRepository.saveAll(List.of(m1, m2));

        return conversation;
    }

    public List<Conversation> getMyConversations(Long userId) {

        List<ConversationMember> members =
                memberRepository.findByUserId(userId);

        return members.stream()
                .map(ConversationMember::getConversation)
                .toList();
    }
}