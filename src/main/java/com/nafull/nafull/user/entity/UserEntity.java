package com.nafull.nafull.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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

    @ElementCollection
    private List<UUID> receivedWelWishIds;

    @ElementCollection
    private List<UUID> sentWelWishIds;

    private Integer totalSpreadCount;

    @ElementCollection
    private List<Integer> spreadCountPerDegree;

    private Integer keyCount;

    public static UserEntity byRegister(
        UUID userId,
        String encodedPassword,
        String discordId
    ) {
        return new UserEntity(
                userId,
                encodedPassword,
                discordId,
                List.of(),
                List.of(),
                0,
                List.of(),
                0
        );
    }
}
