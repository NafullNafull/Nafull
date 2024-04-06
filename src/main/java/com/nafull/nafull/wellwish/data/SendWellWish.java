package com.nafull.nafull.wellwish.data;

import com.nafull.nafull.wellwish.entity.WellWishEntity;

import java.util.UUID;

public record SendWellWish(
    UUID senderId,
    String receiverDiscordId,
    String senderNickname,
    String content
) {
    public WellWishEntity toEntity() {
        return new WellWishEntity(
                senderId,
                receiverDiscordId,
                senderNickname,
                content
        );
    }
}
