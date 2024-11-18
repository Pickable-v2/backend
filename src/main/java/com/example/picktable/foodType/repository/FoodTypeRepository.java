package com.example.picktable.foodType.repository;

import com.example.picktable.foodType.domain.entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {
    Optional<FoodType> findByFoodTypeName(String foodTypeName);
}
