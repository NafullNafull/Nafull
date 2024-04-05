package com.nafull.nafull.wellwish.data;

public record SendWellWish(
    String receiverDiscordId,
    String nickname,
    String content
) {}
