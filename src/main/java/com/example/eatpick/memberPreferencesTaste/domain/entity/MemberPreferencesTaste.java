package com.example.eatpick.memberPreferencesTaste.domain.entity;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.preferencesTaste.domain.entity.PreferencesTaste;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPreferencesTaste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_PREFERENCES_TASTE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PREFERENCES_TASTE_ID", nullable = false)
    private PreferencesTaste preferencesTaste;

    public void addMember(Member member) {
        this.member = member;
    }

    public void addPreferencesTaste(PreferencesTaste preferencesTaste) {
        this.preferencesTaste = preferencesTaste;
    }
}
