package com.chatapp.chat_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.chat_backend.entity.Friendship;
import com.chatapp.chat_backend.enums.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);
    Optional<Friendship> findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_Id(
            Long senderId1, Long receiverId1,
            Long senderId2, Long receiverId2
    );
    List<Friendship> findByReceiver_IdAndStatus(
            Long receiverId,
            FriendshipStatus status
    );
    List<Friendship> findBySender_IdAndStatus(
            Long senderId,
            FriendshipStatus status
    );
    List<Friendship> findBySender_IdOrReceiver_Id(
            Long senderId,
            Long receiverId
    );
    List<Friendship> findBySender_IdAndStatusOrReceiver_IdAndStatus(
            Long senderId,
            FriendshipStatus status1,
            Long receiverId,
            FriendshipStatus status2
    );
}