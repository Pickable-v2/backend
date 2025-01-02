package com.example.eatpick.friendship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.friendship.domain.entity.Friendship;
import com.example.eatpick.friendship.domain.status.FriendshipStatus;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);

    List<Friendship> findByFromMemberIdAndFriendshipStatus(Long fromMemberId, FriendshipStatus friendshipStatus);
}
