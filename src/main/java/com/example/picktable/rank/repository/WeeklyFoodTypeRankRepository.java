package com.example.picktable.rank.repository;

import com.example.picktable.rank.domain.entity.WeeklyFoodTypeRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeeklyFoodTypeRankRepository extends JpaRepository<WeeklyFoodTypeRank, Long> {
}
