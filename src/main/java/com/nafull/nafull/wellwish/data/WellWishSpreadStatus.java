package com.nafull.nafull.wellwish.data;

import java.util.List;

public record WellWishSpreadStatus(
    Integer totalSpreadCount,
    List<Integer> spreadCountPerDegree
) {
    public static WellWishSpreadStatus empty() {
        return new WellWishSpreadStatus(
                0,
                List.of()
        );
    }
}
