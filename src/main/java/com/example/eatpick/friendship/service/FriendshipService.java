package com.example.eatpick.friendship.service;

import org.springframework.stereotype.Service;

import com.example.eatpick.auth.repository.MemberRepository;
import com.example.eatpick.friendship.repository.FriendshipRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipService {
    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;


}
