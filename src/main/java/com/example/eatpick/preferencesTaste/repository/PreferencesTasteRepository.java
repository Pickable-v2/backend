package com.example.eatpick.preferencesTaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eatpick.preferencesTaste.domain.entity.PreferencesTaste;
import com.example.eatpick.preferencesTaste.domain.type.PreferencesType;

public interface PreferencesTasteRepository extends JpaRepository<PreferencesTaste, Long> {
    Optional<PreferencesTaste> findByPreferencesType(PreferencesType preferencesType);
}
