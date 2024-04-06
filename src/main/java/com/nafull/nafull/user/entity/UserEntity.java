package com.nafull.nafull.user.entity;

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
public class UserEntity {
    @Id
    private UUID userId;

    private String encodedPassword;

    @Column(unique = true)
    private String discordId;

    private Integer wingCount;

    private String nickName;

    public static UserEntity byRegister(
        UUID userId,
        String encodedPassword,
        String discordId,
        String nickName
    ) {
        return new UserEntity(
            userId,
            encodedPassword,
            discordId,
            0,
            nickName
        );
    }

    public void addWings(Integer countToAdd) {
        wingCount += countToAdd;
    }

    public void minusWings(Integer countToAdd) {
        wingCount -= countToAdd;
    }
}
