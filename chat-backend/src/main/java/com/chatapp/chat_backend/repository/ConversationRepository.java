package com.chatapp.chat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.chat_backend.entity.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}