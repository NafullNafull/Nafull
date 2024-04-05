package com.nafull.nafull.wellwish.entity;

import com.nafull.nafull.wellwish.data.WellWish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

    public WellWish toDomain() {
        return new WellWish(
            wellWishId,
            senderId,
            receiverDiscordId,
            nickname,
            content,
            locked
        );
    }
}
