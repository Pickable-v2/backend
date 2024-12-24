package com.example.eatpick.preferencesTaste.domain.type;

public enum PreferencesType {
    SPICY("매운 음식"),
    SWEET("단 음식"),
    SALTY("짠 음식"),
    SOUR("신 음식"),
    SAVORY("감칠맛 나는 음식"),
    MILD("순한 음식"),
    PLAIN("담백한 음식"),
    VEGAN("채식 음식"),
    VEGETARIAN("베지테리언 음식"),
    SEAFOOD("해산물"),
    MEAT_LOVER("고기 요리"),
    DAIRY_FREE("유제품 제외"),
    GLUTEN_FREE("글루텐 프리"),
    LOW_CARB("저탄수화물 음식"),
    ORGANIC("유기농 음식"),
    TRADITIONAL("전통 음식"),
    ;

    private final String name;

    PreferencesType(String name) {
        this.name = name;
    }

    public static PreferencesType from(PreferencesType preferencesType) {
        for (PreferencesType type : values()) {
            if (type == preferencesType) {
                return type;
            }
        }
        throw new IllegalArgumentException("올바르지 않은 선호도 타입입니다 : " + preferencesType);
    }
}
