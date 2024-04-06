package com.nafull.nafull.user.data;

import java.util.UUID;

public record RegisterUser(
    UUID letterId,
    String rawPassword,
    Boolean personalInformationAgreement,
    String nickName
) {}
