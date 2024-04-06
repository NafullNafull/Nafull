package com.nafull.nafull.user.data;

import com.nafull.nafull.wellwish.data.WellWish;

import java.util.List;
import java.util.UUID;

public record User(
    UUID userId,
    String discordId,
    List<WellWish> receivedWellWishes,
    List<WellWish> sentWellWishes,
    Long totalSpreadCount,
    Integer keyCount
) { }
