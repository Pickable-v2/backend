package com.example.eatpick.auth.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.eatpick.auth.domain.dto.request.MyPageUpdateRequest;
import com.example.eatpick.auth.domain.type.GenderType;
import com.example.eatpick.auth.domain.type.RoleType;
import com.example.eatpick.memberPreferencesTaste.domain.entity.MemberPreferencesTaste;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    @Column(nullable = false)
    private String verifiedLoginPw;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private int age;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberPreferencesTaste> preferences = new ArrayList<>();

    @Builder
    public Member(String loginId, String loginPw, String verifiedLoginPw, String nickname, GenderType gender,
        RoleType role,
        int age) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.verifiedLoginPw = verifiedLoginPw;
        this.nickname = nickname;
        this.gender = gender;
        this.role = role;
        this.age = age;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.loginPw = passwordEncoder.encode(loginPw);
    }

    public void update(MyPageUpdateRequest request) {
        this.nickname = request.nickname();
        this.gender = request.gender();
        this.age = request.age();
    }

    public void addPreferences(MemberPreferencesTaste preferences) {
        this.preferences.add(preferences);
        preferences.addMember(this);
    }

    public void removePreferences(MemberPreferencesTaste preferences) {
        this.preferences.remove(preferences);
        preferences.addMember(null);
    }
}
