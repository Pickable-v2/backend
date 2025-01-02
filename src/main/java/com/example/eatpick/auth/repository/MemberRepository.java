package com.example.eatpick.auth.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.friendship.domain.dto.SearchResponse;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    Page<SearchResponse> findByLoginIdContaining(String loginId, Pageable pageable);
}
