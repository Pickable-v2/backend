package com.example.picktable.likes.repository;

import com.example.picktable.likes.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByReviewIdAndMemberIdAndState(Long reviewId, Long memberId, boolean state);
    Optional<Likes> findByReviewIdAndMemberIdAndState(Long reviewId, Long memberId, boolean state);
}
