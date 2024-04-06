package com.nafull.nafull.user.data;

import java.util.List;
import java.util.UUID;

public record UserRelation(
    UUID userId,
    List<UUID> relateUserIds //user who signed up through relation owner
) {}
