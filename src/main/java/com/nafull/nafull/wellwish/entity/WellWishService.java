package com.nafull.nafull.wellwish.entity;

import com.nafull.nafull.discord.DiscordService;
import com.nafull.nafull.wellwish.WellWishRepository;
import com.nafull.nafull.wellwish.data.SendWellWish;
import com.nafull.nafull.wellwish.data.WellWish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WellWishService {
    private final WellWishRepository wellWishRepository;
    private final DiscordService discordService;

    String BASE_URL = "https://localhost:8080/api/v1/";

    public WellWish findOne(String wellWishId) {
        // 마음함에 담겨있는 편지가 unlock 된 상태였을 때만 볼 수 있음
        UUID wishId = UUID.fromString(wellWishId);
        WellWish wish = wellWishRepository.findById(wishId)
                .orElseThrow(() -> new RuntimeException("WellWish not found")).toDomain();
        if (wish.locked()) {
            throw new RuntimeException("WellWish locked");
        }
        return wish;
    }

    public void sendLetter(List<SendWellWish> data) {
        // db에 request.data가 저장 > 문자의 uuid를 사용하여 url을 디스코드 봇으로 보내줌
        data.stream()
                .map(SendWellWish::toEntity)
                .forEach(wish -> {
                    wellWishRepository.save(wish);
                    discordService.sendMessage(wish.getReceiverDiscordId(),
                            wish.getNickname(),
                            BASE_URL + wish.getWellWishId());
                });
    }
}
