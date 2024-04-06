package com.nafull.nafull.wellwish.data;

import java.util.UUID;

public record SendWellWish(
    UUID senderId,
    String receiverDiscordId,
    String senderNickname,
    String content
) {}
