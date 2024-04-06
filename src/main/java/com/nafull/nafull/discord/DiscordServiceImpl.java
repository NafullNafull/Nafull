package com.nafull.nafull.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

@Service
public class DiscordServiceImpl implements DiscordService {
    private final JDA jda;
    private final String guildId;

    public DiscordServiceImpl(
        JDA jda,
        @Value("${discord.guild-id}")
        String guildId
    ) {
        this.jda = jda;
        this.guildId = guildId;
    }


    @Override
    public void sendMessage(String senderNickname, String receiverDiscordId, String letterUri) {
        MessageEmbed message = createMessage(senderNickname);
        Button button = createButton(letterUri);

        withPrivateChannelByDiscordId(receiverDiscordId, channel -> sendMessage(channel, message, button));
    }

    private void withPrivateChannelByDiscordId(
            String discordId,
            Function<PrivateChannel, MessageAction> consumer
    ) {
        Guild guild = Objects.requireNonNull(jda.getGuildById(guildId));
        guild.loadMembers().onSuccess(members -> {
            Member member = members.stream()
                    .filter(m -> m.getEffectiveName().equals(discordId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("discord user id not found!")); //TODO

            member.getUser().openPrivateChannel()
                    .flatMap(consumer)
                    .queue();
        });
    }

    private MessageEmbed createMessage(String senderNickname) {
        return new EmbedBuilder()
                .setTitle(senderNickname + "님이 보낸 마음편지가 도착했어요!")
                .setDescription("지금 바로 " + senderNickname + "님의 편지를 확인해보세요!")
                .setColor(Color.PINK)
                .setAuthor("@나풀")
                .build();
    }

    private Button createButton(String letterUri) {
        return Button.link(letterUri, "마음편지 보러가기")
                .withEmoji(Emoji.fromUnicode("\uD83E\uDD8B"));
    }

    private MessageAction sendMessage(PrivateChannel channel, MessageEmbed message, Button button) {
        return channel.sendMessageEmbeds(message).setActionRow(button);
    }
}
