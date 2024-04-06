package com.nafull.nafull.wellwish.entity;

import com.nafull.nafull.wellwish.data.SendWellWish;
import com.nafull.nafull.wellwish.data.WellWish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static com.nafull.nafull.wellwish.data.WellWish.LOCKED_CONTENT;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WellWishEntity {
    @Id
    private UUID wellWishId;

    private UUID senderId;

    private String receiverDiscordId;

    private String nickname;

    private String content;

    private Boolean locked;

    public static WellWishEntity from(SendWellWish wish) {
        return new WellWishEntity(
            UUID.randomUUID(),
            wish.senderId(),
            wish.receiverDiscordId(),
            wish.senderNickname(),
            wish.content(),
            false
        );
    }

    public WellWish toDomainWithContentLock() {
        return new WellWish(
            wellWishId,
            senderId,
            receiverDiscordId,
            nickname,
            locked ? LOCKED_CONTENT : content,
            locked
        );
    }
}
