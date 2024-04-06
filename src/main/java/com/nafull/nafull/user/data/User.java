package com.nafull.nafull.user.data;

import com.nafull.nafull.letter.data.Letter;

import java.util.List;
import java.util.UUID;

public record User(
    UUID userId,
    String discordId,
    List<Letter> receivedLetters,
    List<Letter> sentLetters,
    Long totalSpreadCount,
    Integer keyCount
) { }
