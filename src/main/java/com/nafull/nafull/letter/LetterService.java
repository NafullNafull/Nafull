package com.nafull.nafull.letter;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.common.error.ErrorCode;
import com.nafull.nafull.common.error.WebException;
import com.nafull.nafull.discord.DiscordService;
import com.nafull.nafull.letter.data.BadgeType;
import com.nafull.nafull.letter.data.Letter;
import com.nafull.nafull.letter.data.ReceiveLetter;
import com.nafull.nafull.letter.data.SendLetter;
import com.nafull.nafull.letter.entity.LetterEntity;
import com.nafull.nafull.user.DefaultUser;
import com.nafull.nafull.user.UserService;
import com.nafull.nafull.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class LetterService {
    private static final Integer WING_COUNT_REQUIRED_TO_UNLOCK = 2;
    private final LetterRepository letterRepository;
    private final UserService userService;
    private final DiscordService discordService;
    private final DefaultUser defaultUser;
    private final String letterBaseUri;

    public LetterService(
            LetterRepository letterRepository,
            UserService userService,
            DiscordService discordService,
            DefaultUser defaultUser,
            @Value("${letter.base-uri}")
            String letterBaseUri
    ) {
        this.letterRepository = letterRepository;
        this.userService = userService;
        this.discordService = discordService;
        this.defaultUser = defaultUser;
        this.letterBaseUri = letterBaseUri;
    }

    private String generateLetterURI(LetterEntity entity) {
        return this.letterBaseUri + "/" + entity.getLetterId();
    }

    public Letter findOne(UUID letterId) {;
        return letterRepository.findById(letterId)
            .orElseThrow(() -> new WebException("마음편지를 찾을 수 없어요", ErrorCode.LETTER_NOT_FOUND))
            .toDomainWithContentLock();
    }

    @Transactional
    public void receive(ReceiveLetter request) {
        String name = discordService.getUserNameByDiscordId(request.discordId());
        String staticContent = LetterContents.generateContent(name);
        int badgeIdx = new Random().nextInt(BadgeType.values().length);
        BadgeType badge = BadgeType.values()[badgeIdx];

        SendLetter sendLetter = new SendLetter(
            defaultUser.getId(),
            request.discordId(),
            defaultUser.getNickname(),
            staticContent,
            badge
        );
        send(new ListData<>(List.of(sendLetter)));
    }

    @Transactional
    public void send(ListData<SendLetter> list) {
        List<LetterEntity> entities = list.data().stream()
            .map(LetterEntity::from).toList();

        Map<UUID, Integer> wingsBySender = new HashedMap<>();
        entities.forEach(entity -> {
            UUID senderId = entity.getSenderId();
            wingsBySender.put(senderId, wingsBySender.getOrDefault(senderId, 0) + 1);
        });

        List<LetterEntity> created = letterRepository.saveAll(entities);

        wingsBySender.entrySet().parallelStream().forEach(entry ->
            userService.addWings(entry.getKey(), entry.getValue())
        );

        created.parallelStream().forEach(wish -> {
            discordService.sendMessage(
                wish.getNickname(),
                wish.getReceiverDiscordId(),
                generateLetterURI(wish)
            );
        });
    }

    @Transactional
    public void unlock(UUID letterId, UUID userId) {
        LetterEntity letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new WebException("마음편지를 찾을 수 없어요", ErrorCode.LETTER_NOT_FOUND));
        final UserEntity user = userService.findById(userId);

        if(!user.getDiscordId().equals(letter.getReceiverDiscordId()))
            throw new WebException("마음편지를 찾을 수 없어요", ErrorCode.LETTER_NOT_FOUND);

        if(user.getWingCount() < WING_COUNT_REQUIRED_TO_UNLOCK)
            throw new WebException("날갯짓이 부족해요", ErrorCode.NOT_ENOUGH_WINGS);

        letter.setLocked(false);

        userService.minusWings(userId, 2);
        letterRepository.save(letter);
    }
}
