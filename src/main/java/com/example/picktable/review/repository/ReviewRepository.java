package com.example.picktable.review.repository;

import com.example.picktable.review.domain.entity.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.restaurant rest WHERE rest.addressNumber LIKE %:address% ORDER BY r.createdDate DESC")
    Page<Review> findByAddress(@Param("address") String address, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.restaurant.id = :id")
    Page<Review> findByRestaurantforPage(@Param("id") Long restaurantId, Pageable pageable);
}

