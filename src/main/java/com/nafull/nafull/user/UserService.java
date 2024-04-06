package com.nafull.nafull.user;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.common.ListUtil;
import com.nafull.nafull.user.data.RegisterUser;
import com.nafull.nafull.user.data.User;
import com.nafull.nafull.user.data.UserRelation;
import com.nafull.nafull.user.entity.UserEntity;
import com.nafull.nafull.user.entity.UserRelationEntity;
import com.nafull.nafull.wellwish.WellWishRepository;
import com.nafull.nafull.wellwish.data.WellWish;
import com.nafull.nafull.wellwish.entity.WellWishEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;
    private final PasswordEncoder encoder;
    private final WellWishRepository wellWishRepository;

    public User findOneByDiscordId(String discordId) {
        final UserEntity entity = userRepository.findByDiscordId(discordId)
                .orElseThrow(() -> new RuntimeException("user not found!")); //TODO

        return aggregateToUser(entity);
    }

    public ListData<String> findAllDiscordIds() {
        final List<UserEntity> users = userRepository.findAll();
        final List<String> discordIds = users.stream().map(UserEntity::getDiscordId).toList();
        return new ListData<>(discordIds);
    }

    @Transactional
    public User register(RegisterUser dto) {
        final WellWishEntity wellWish =
            wellWishRepository.findById(dto.wellWishId())
                .orElseThrow(() -> new RuntimeException("wellwish not found")); //TODO

        final String discordId = wellWish.getReceiverDiscordId();

        final boolean alreadyRegisteredDiscordId = userRepository.findByDiscordId(discordId).isPresent();
        if(alreadyRegisteredDiscordId)
            throw new RuntimeException("already registered"); //TODO

        final UUID userId = UUID.randomUUID();
        final String password = encoder.encode(dto.rawPassword());

        final UserEntity user = UserEntity.byRegister(userId, password, discordId);

        final UUID spreaderId = wellWish.getSenderId();
        final UserRelation spreaderRelation = findUserRelation(spreaderId);
        final List<UUID> newRelateUserIds = ListUtil.merge(spreaderRelation.relateUserIds(), userId);
        final List<UserRelationEntity> newUserRelations = newRelateUserIds.stream().map(relateUserId ->
            new UserRelationEntity(
                UUID.randomUUID(),
                userId,
                relateUserId
            )
        ).toList();

        userRelationRepository.saveAll(newUserRelations);
        final UserEntity created = userRepository.save(user);

        return new User(
            created.getUserId(),
            created.getDiscordId(),
            List.of(),
            List.of(),
            0L,
            0
        );
    }

    public User login(LoginUser request) {
        final UserEntity entity = userRepository.findByDiscordId(request.discordId())
                .orElseThrow(() -> new RuntimeException("wrong id or password")); //TODO

        final String password = entity.getEncodedPassword();
        final boolean isPasswordMatch = encoder.matches(request.rawPassword(), password);

        if(!isPasswordMatch)
            throw new RuntimeException("wrong id or password"); //TODO

        return aggregateToUser(entity);
    }

    public Long calculateUserTotalSpreadCount(UUID userId) {
        final List<UserRelationEntity> list = userRelationRepository.findAllByRelateUserId(userId);
        return list.stream()
                .map(UserRelationEntity::getUserId)
                .distinct()
                .filter(id -> id != userId)
                .count();
    }

    private UserRelation findUserRelation(UUID userId) {
        final List<UserRelationEntity> entities = userRelationRepository.findAllByUserId(userId);
        final List<UUID> relateUserIds = entities.stream().map(UserRelationEntity::getRelateUserId).toList();
        return new UserRelation(userId, relateUserIds);
    }

    private User aggregateToUser(UserEntity entity) {
        final List<WellWish> receivedWllWishes = wellWishRepository.findAllById(
                entity.getReceivedWelWishIds()
        ).stream().map(WellWishEntity::toDomain).toList();

        final List<WellWish> sentWellWishes = wellWishRepository.findAllById(
                entity.getSentWelWishIds()
        ).stream().map(WellWishEntity::toDomain).toList();

        return new User(
                entity.getUserId(),
                entity.getDiscordId(),
                receivedWllWishes,
                sentWellWishes,
                calculateUserTotalSpreadCount(entity.getUserId()),
                entity.getKeyCount()
        );
    }
}
