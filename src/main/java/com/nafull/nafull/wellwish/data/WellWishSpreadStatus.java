package com.nafull.nafull.wellwish.data;

import java.util.List;

public record WellWishSpreadStatus(
    Integer totalSpreadCount,
    List<Integer> spreadCountPerDegree
) {}
