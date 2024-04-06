package com.nafull.nafull.letter.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nafull.nafull.common.error.ErrorCode;
import com.nafull.nafull.common.error.WebException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum BadgeType {
    BUTTERFLY("butterfly"),
    CLOVER("clover"),
    KEY("key"),
    FLOWER("flower"),
    LETTER("letter");

    private final String name;

    @JsonCreator
    public static BadgeType deserialize(String str) {
        return Arrays.stream(values())
                .filter(el -> Objects.equals(el.name, str)).findFirst()
                .orElseThrow(() -> new WebException("뱃지를 찾을 수 없어요", ErrorCode.BADGE_NOT_FOUND));
    }
}
