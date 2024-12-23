package com.example.eatpick.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.auth.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
