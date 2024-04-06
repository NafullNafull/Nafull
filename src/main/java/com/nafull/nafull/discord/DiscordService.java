package com.nafull.nafull.discord;

public interface DiscordService {
    void sendMessage(String senderNickname, String receiverDiscordId, String letterUri);
    String getUserNameByDiscordId(String discordId);
}

