package com.nafull.nafull.wellwish.entity;

import com.nafull.nafull.wellwish.WellWishRepository;
import com.nafull.nafull.wellwish.data.WellWish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WellWishService {
    private final WellWishRepository wellWishRepository;

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
}
