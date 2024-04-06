package com.nafull.nafull.wellwish.entity;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.discord.DiscordService;
import com.nafull.nafull.user.UserService;
import com.nafull.nafull.user.entity.UserEntity;
import com.nafull.nafull.user.DefaultUser;
import com.nafull.nafull.wellwish.WellWishRepository;
import com.nafull.nafull.wellwish.data.ReceiveWellWish;
import com.nafull.nafull.wellwish.data.SendWellWish;
import com.nafull.nafull.wellwish.data.WellWish;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WellWishService {
    private static final Integer WING_COUNT_REQUIRED_TO_UNLOCK = 2;
    private final WellWishRepository wellWishRepository;
    private final UserService userService;
    private final DiscordService discordService;
    private final DefaultUser defaultUser;

    private static String generateLetterURI(WellWishEntity entity) {
        String BASE_URL = "https://localhost:8080/api/v1/"; //FIXME
        return BASE_URL + entity.getWellWishId();
    }

    public WellWish findOne(UUID wellWishId) {;
        return wellWishRepository.findById(wellWishId)
                .orElseThrow(() -> new RuntimeException("WellWish not found"))
                .toDomainWithContentLock();
    }

    @Transactional
    public void receive(ReceiveWellWish request) {
        String staticContent = "[병역통지서] 이것은 혼신의 힘을 담아 작성한 당신만을 위한 이등병의 편지입니다.";
        SendWellWish sendWellWish = new SendWellWish(
            defaultUser.getId(),
            request.discordId(),
            defaultUser.getNickname(),
            staticContent
        );
        send(new ListData<>(List.of(sendWellWish)));
    }

    @Transactional
    public void send(ListData<SendWellWish> list) {
        List<WellWishEntity> entities = list.data().stream()
            .map(WellWishEntity::from).toList();

        Map<UUID, Integer> wingsBySender = new HashedMap<>();
        entities.forEach(entity -> {
            UUID senderId = entity.getSenderId();
            wingsBySender.put(senderId, wingsBySender.getOrDefault(senderId, 0) + 1);
        });

        List<WellWishEntity> created = wellWishRepository.saveAll(entities);

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
    public void unlock(UUID wellWishId, UUID userId) {
        WellWishEntity wellWish = wellWishRepository.findById(wellWishId)
            .orElseThrow(() -> new RuntimeException("WellWish not found!")); //TODO
        final UserEntity user = userService.findById(userId);

        if(!user.getDiscordId().equals(wellWish.getReceiverDiscordId()))
            throw new RuntimeException("permission denied."); //TODO

        if(user.getWingCount() < WING_COUNT_REQUIRED_TO_UNLOCK)
            throw new RuntimeException("not enough wings");

        wellWish.setLocked(false);

        userService.minusWings(userId, 2);
        wellWishRepository.save(wellWish);
    }
}
