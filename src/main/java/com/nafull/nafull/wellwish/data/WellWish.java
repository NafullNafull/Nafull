package com.nafull.nafull.wellwish.data;

import java.util.Objects;
import java.util.UUID;

public record WellWish(
    UUID wellWishId,
    UUID senderId,
    UUID receiverId,
    String nickname,
    String content,
    Boolean locked
) {
    public static String LOCKED_CONTENT = "";
    public WellWish {
        if(locked && !Objects.equals(content, LOCKED_CONTENT))
            throw new RuntimeException(
                " content must be equal LOCKED_CONTENT when well-wish is locked"
            );
    }
}
