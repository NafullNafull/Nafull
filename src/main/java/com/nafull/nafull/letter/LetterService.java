package com.nafull.nafull.letter;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.discord.DiscordService;
import com.nafull.nafull.letter.entity.LetterEntity;
import com.nafull.nafull.user.UserService;
import com.nafull.nafull.user.entity.UserEntity;
import com.nafull.nafull.user.DefaultUser;
import com.nafull.nafull.letter.data.ReceiveLetter;
import com.nafull.nafull.letter.data.SendLetter;
import com.nafull.nafull.letter.data.Letter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LetterService {
    private static final Integer WING_COUNT_REQUIRED_TO_UNLOCK = 2;
    private final LetterRepository letterRepository;
    private final UserService userService;
    private final DiscordService discordService;
    private final DefaultUser defaultUser;

    private static String generateLetterURI(LetterEntity entity) {
        String BASE_URL = "https://localhost:8080/api/v1/"; //FIXME
        return BASE_URL + entity.getLetterId();
    }

    public Letter findOne(UUID letterId) {;
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new RuntimeException("letter not found")) //TODo
                .toDomainWithContentLock();
    }

    @Transactional
    public void receive(ReceiveLetter request) {
        String staticContent = "[병역통지서] 이것은 혼신의 힘을 담아 작성한 당신만을 위한 이등병의 편지입니다.";
        SendLetter sendLetter = new SendLetter(
            defaultUser.getId(),
            request.discordId(),
            defaultUser.getNickname(),
            staticContent
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
            .orElseThrow(() -> new RuntimeException("letter not found!")); //TODO
        final UserEntity user = userService.findById(userId);

        if(!user.getDiscordId().equals(letter.getReceiverDiscordId()))
            throw new RuntimeException("permission denied."); //TODO

        if(user.getWingCount() < WING_COUNT_REQUIRED_TO_UNLOCK)
            throw new RuntimeException("not enough wings");

        letter.setLocked(false);

        userService.minusWings(userId, 2);
        letterRepository.save(letter);
    }
}
