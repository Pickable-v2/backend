package com.example.eatpick.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.friendship.domain.entity.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}
