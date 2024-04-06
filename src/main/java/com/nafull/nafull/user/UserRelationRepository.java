package com.nafull.nafull.user;

import com.nafull.nafull.user.entity.UserRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRelationRepository extends JpaRepository<UserRelationEntity, UUID> {
    List<UserRelationEntity> findAllByRelateUserIdContains(UUID relateUserId);
    List<UserRelationEntity> findAllByUserId(UUID userId);
}
