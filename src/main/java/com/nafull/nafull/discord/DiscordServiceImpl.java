package com.nafull.nafull.discord;

import com.nafull.nafull.common.error.ErrorCode;
import com.nafull.nafull.common.error.WebException;
import com.nafull.nafull.letter.data.BadgeType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
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
    public void sendMessage(
        String senderNickname,
        String receiverDiscordId,
        String letterUri,
        BadgeType badge
    ) {
        MessageEmbed message = createMessage(senderNickname, badge);
        Button button = createButton(letterUri);

        withMemberByDiscordId(receiverDiscordId, member ->
            member.getUser().openPrivateChannel()
                .flatMap(channel -> sendMessage(channel, message, button))
        ).queue();
    }

    @Override
    public String getUserNameByDiscordId(String discordId) {
        String nickname = withMemberByDiscordId(discordId, member -> member.getUser().getName());
        String pattern = "\\[([^\\[\\]]+)\\]";
        return nickname.replaceAll(pattern, "").trim();
    }

    private <T> T withMemberByDiscordId(
        String discordId,
        Function<Member, T> consumer
    ) {
        Guild guild = Objects.requireNonNull(jda.getGuildById(guildId));
        List<Member> members = guild.loadMembers().get();
        Member member = members.stream()
                .filter(m -> m.getEffectiveName().equals(discordId))
                .findFirst()
                .orElseThrow(() -> new WebException("디스코드 유저를 찾을 수 없어요!", ErrorCode.DISCORD_USER_NOT_FOUND));

        return consumer.apply(member);
    }

    private MessageEmbed createMessage(String senderNickname, BadgeType badge) {
        return new EmbedBuilder()
            .setTitle(senderNickname + "님이 보낸 마음편지가 도착했어요!")
            .setDescription("지금 바로 " + senderNickname + "님의 편지를 확인해보세요!")
            .setImage(badge.getImageUrl())
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
