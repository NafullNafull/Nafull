package com.nafull.nafull.discord;

import org.springframework.stereotype.Service;

@Service
public class DummyDiscordService implements DiscordService {
    @Override
    public void sendMessage(String senderNickname, String receiverDiscordId, String wellWishUri) {
        System.out.printf("sendMessage(%s, %s, %s)\n", senderNickname, receiverDiscordId, wellWishUri);
    }
}
