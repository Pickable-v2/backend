package com.example.picktable.rank.repository;

import com.example.picktable.rank.domain.entity.WeeklyFoodRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyFoodRankRepository extends JpaRepository<WeeklyFoodRank, Long> {
}
