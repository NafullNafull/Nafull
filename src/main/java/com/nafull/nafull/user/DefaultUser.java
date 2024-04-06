package com.nafull.nafull.user;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
public class DefaultUser {
    private final UUID id;
    private final String discordId;
    private final String nickname;
    private final String rawPassword;

    public DefaultUser(
        @Value("${user.default.id}")
        String id,
        @Value("${user.default.discord-id}")
        String discordId,
        @Value("${user.default.nickname}")
        String nickname,
        @Value("${user.default.password}")
        String rawPassword
    ) {
        this.id = UUID.fromString(id);
        this.discordId = discordId;
        this.nickname = nickname;
        this.rawPassword = rawPassword;
    }
}
