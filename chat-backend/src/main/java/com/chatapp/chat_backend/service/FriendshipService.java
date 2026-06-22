package com.chatapp.chat_backend.service;

import org.springframework.stereotype.Service;

import com.chatapp.chat_backend.entity.Friendship;
import com.chatapp.chat_backend.entity.User;
import com.chatapp.chat_backend.enums.FriendshipStatus;
import com.chatapp.chat_backend.repository.FriendshipRepository;
import com.chatapp.chat_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public void sendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Không thể kết bạn với chính mình");
        }

        boolean exists = friendshipRepository
                .findBySender_IdAndReceiver_Id(senderId, receiverId)
                .isPresent()
            || friendshipRepository
                .findBySender_IdAndReceiver_Id(receiverId, senderId)
                .isPresent();

        if (exists) {
            throw new RuntimeException("Đã tồn tại lời mời kết bạn");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Friendship friendship = Friendship.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friendship);
    }

    public void acceptRequest(Long senderId, Long receiverId) {

        Friendship friendship = friendshipRepository
                .findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (!friendship.getReceiver().getId().equals(receiverId)) {
            throw new RuntimeException("Not allowed");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);

        friendshipRepository.save(friendship);
    }
}