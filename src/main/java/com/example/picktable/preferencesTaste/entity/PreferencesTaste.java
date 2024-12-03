package com.example.picktable.preferencesTaste.entity;

import com.example.picktable.preferencesTaste.type.PreferencesType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class PreferencesTaste {

    @Id
    @GeneratedValue
    private Long id;

    private PreferencesType preferencesType;
    private String preferencesLocation;
    private String preferencesTIme;
}
