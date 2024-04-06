package com.nafull.nafull.discord;

import com.nafull.nafull.letter.data.BadgeType;

public interface DiscordService {
    void sendMessage(
        String senderNickname,
        String receiverDiscordId,
        String letterUri,
        BadgeType badge
    );

    String getUserNameByDiscordId(String discordId);
}

