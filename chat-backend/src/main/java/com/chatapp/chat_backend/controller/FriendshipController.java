package com.chatapp.chat_backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.chat_backend.dto.frienship.FriendRequest;
import com.chatapp.chat_backend.enums.FriendshipStatus;
import com.chatapp.chat_backend.repository.FriendshipRepository;
import com.chatapp.chat_backend.service.FriendshipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository;

    // =========================
    // GỬI LỜI MỜI KẾT BẠN
    // =========================
    @GetMapping("/requests")
    public List<FriendRequest> getRequests(Authentication auth) {

        Long userId = Long.valueOf(auth.getPrincipal().toString());

        return friendshipRepository
                .findByReceiver_IdAndStatus(userId, FriendshipStatus.PENDING)
                .stream()
                .map(f -> new FriendRequest(
                        f.getId(),
                        f.getSender().getId(),
                        f.getSender().getUsername()
                ))
                .toList();
    }

    // =========================
    // CHẤP NHẬN LỜI MỜI
    // =========================
    @PostMapping("/accept")
    public void acceptRequest(
            @RequestBody FriendRequest request,
            Authentication auth) {

        Long receiverId = (Long) auth.getPrincipal();

        friendshipService.acceptRequest(
                request.getSenderId(),
                receiverId
        );
    }
}