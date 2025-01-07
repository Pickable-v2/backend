package com.example.eatpick.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.restaurant.domain.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContaining(String name, Pageable pageable);
    Page<Restaurant> findByMenusContaining(String menus, Pageable pageable);
}
