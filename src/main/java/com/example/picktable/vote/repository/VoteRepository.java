package com.example.picktable.vote.repository;

import com.example.picktable.vote.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
