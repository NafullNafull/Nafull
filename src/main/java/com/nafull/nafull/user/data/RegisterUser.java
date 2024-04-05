package com.nafull.nafull.user.data;

import java.util.UUID;

public record RegisterUser(
    UUID wellWishId,
    String rawPassword,
    Boolean personalInformationAgreement
) {}
