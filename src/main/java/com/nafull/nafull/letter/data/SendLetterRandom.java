package com.nafull.nafull.letter.data;

import java.util.UUID;

public record SendLetterRandom(
        UUID senderId,
        String senderNickname,
        String content,
        BadgeType badge
) {}