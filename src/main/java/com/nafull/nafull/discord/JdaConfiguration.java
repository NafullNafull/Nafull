package com.nafull.nafull.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class JdaConfiguration {
    @Bean
    public JDA jda(@Value("${discord.token}") String token) {
        try {
            return JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        } catch (LoginException e) {
            throw new RuntimeException("Login Failed! please check jda.token value.", e);
        }
    }
}
