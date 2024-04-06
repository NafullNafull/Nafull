package com.nafull.nafull.user;

import com.nafull.nafull.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByDiscordId(String discordId);

    @Query(value = "SELECT * FROM user_entity u ORDER BY RAND() LIMIT 1", nativeQuery = true)
    UserEntity findOneRandomUserId();
}
