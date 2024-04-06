package com.nafull.nafull.discord;

import org.springframework.stereotype.Service;

@Service
public class DummyDiscordService implements DiscordService {
    @Override
    public void sendMessage(String discordUserId, String wellWishUri) {
        System.out.printf("sendMessage(%s, %s)\n", discordUserId, wellWishUri);
    }
}
