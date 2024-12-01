package com.example.picktable.meet.domain.entity;

import jakarta.persistence.*;
import com.example.picktable.food.domain.entity.Food;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Meet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEET_ID")
    private Long id;

    private String meetLocate;
    private String meetMenu;
    private String meetTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "food_id")
    private Food food;

    @Builder
    public Meet(String meetLocate, String meetMenu, String meetTime) {
        this.meetLocate = meetLocate;
        this.meetMenu = meetMenu;
        this.meetTime = meetTime;
    }

    public static Meet createMeet(String meetLocate, String meetTime) {
        return Meet.builder()
                .meetLocate(meetLocate)
                .meetTime(meetTime)
                .build();
    }

    public void updateMeet(String meetLocate, String meetTime) {
        this.meetLocate = meetLocate;
        this.meetTime = meetTime;
    }

    public void setMeetMenu(String meetMenu) {
        this.meetMenu = meetMenu;
    }
}
