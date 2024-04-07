package com.nafull.nafull.letter.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.nafull.nafull.common.error.ErrorCode;
import com.nafull.nafull.common.error.WebException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum BadgeType {
    BUTTERFLY("butterfly", "https://i.ibb.co/ZhVnh9p/image.png"),
    CLOVER("clover", "https://i.ibb.co/Cpc1vXF/image.png"),
    KEY("key", "https://i.ibb.co/DKkmww8/Frame-99.png"),
    FLOWER("flower", "https://i.ibb.co/DG2v5sz/Frame-98.png"),
    LETTER("letter", "https://i.ibb.co/DpQ6HBN/Frame-100.png");

    private final String name;
    private final String imageUrl;

    @JsonCreator
    public static BadgeType deserialize(String str) {
        return Arrays.stream(values())
                .filter(el -> Objects.equals(el.name, str)).findFirst()
                .orElseThrow(() -> new WebException("뱃지를 찾을 수 없어요", ErrorCode.BADGE_NOT_FOUND));
    }

    @Override
    @JsonValue
    public String toString() {
        return name;
    }
}
