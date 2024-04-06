package com.nafull.nafull.letter.data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record Letter(
    UUID letterId,
    UUID senderId,
    String receiverDiscordId,
    String nickname,
    String content,
    BadgeType badge,
    Boolean locked,
    LocalDateTime createAt
) {
    public static String LOCKED_CONTENT = "";
    public Letter {
        if(locked && !Objects.equals(content, LOCKED_CONTENT))
            throw new RuntimeException(
                " content must be equal LOCKED_CONTENT when letter is locked"
            );
    }
}
