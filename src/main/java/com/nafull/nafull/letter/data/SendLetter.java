package com.nafull.nafull.letter.data;

import java.util.UUID;

public record SendLetter(
    UUID senderId,
    String receiverDiscordId,
    String senderNickname,
    String content
) {}
