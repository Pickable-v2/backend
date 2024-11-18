package com.example.picktable.foodType.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.restaurant.domain.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FoodType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOODTYPE_ID")
    private Long id;

    private String foodTypeName;
    private Long count;

    @OneToMany(mappedBy = "foodType",fetch = FetchType.EAGER)
    private List<Restaurant>  restaurants;
}
