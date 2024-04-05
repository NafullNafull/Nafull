package com.nafull.nafull.user.data;

import com.nafull.nafull.wellwish.data.WellWishSpreadStatus;
import com.nafull.nafull.wellwish.data.WellWish;

import java.util.List;
import java.util.UUID;

public record User(
    UUID userId,
    String discordId,
    List<WellWish> receivedWelWishes,
    List<WellWish> sentWelWishes,
    WellWishSpreadStatus wellWishSpreadStatus,
    Integer keyCount
) {
}
