package com.example.eatpick.preferencesTaste.domain.entity;

import com.example.eatpick.preferencesTaste.domain.type.PreferencesType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreferencesTaste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PREFERENCES_TASTE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PreferencesType preferencesType;
}
