package com.example.picktable.friendship.repository;

import com.example.picktable.friendship.domain.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findOneByMemberLoginIdAndFriendLoginId(String memberLoginId, String friendLoginId);
}
