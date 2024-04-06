package com.nafull.nafull.wellwish;

import com.nafull.nafull.wellwish.entity.WellWishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WellWishRepository extends JpaRepository<WellWishEntity, UUID> {
    List<WellWishEntity> findAllByReceiverDiscordId(String receiverDiscordId);
    List<WellWishEntity> findAllBySenderId(UUID senderId);
}
