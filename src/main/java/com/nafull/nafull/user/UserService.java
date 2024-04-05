package com.nafull.nafull.user;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.user.data.RegisterUser;
import com.nafull.nafull.user.data.User;
import com.nafull.nafull.user.entity.UserEntity;
import com.nafull.nafull.wellwish.WellWishRepository;
import com.nafull.nafull.wellwish.data.WellWish;
import com.nafull.nafull.wellwish.data.WellWishSpreadStatus;
import com.nafull.nafull.wellwish.entity.WellWishEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final WellWishRepository wellWishRepository;

    public User findOneByDiscordId(String discordId) {
        final UserEntity entity = repository.findByDiscordId(discordId)
                .orElseThrow(() -> new RuntimeException("user not found!")); //TODO

        return aggregateToUser(entity);
    }

    public ListData<String> findAllDiscordIds() {
        final List<UserEntity> users = repository.findAll();
        final List<String> discordIds = users.stream().map(UserEntity::getDiscordId).toList();
        return new ListData<>(discordIds);
    }

    public User register(RegisterUser dto) {
        final WellWishEntity wellWish =
            wellWishRepository.findById(dto.wellWishId())
                .orElseThrow(() -> new RuntimeException("wellwish not found")); //TODO

        String discordId = wellWish.getReceiverDiscordId();
        if(repository.findByDiscordId(discordId).isPresent())
            throw new RuntimeException("already registered"); //TODO

        final UUID userId = UUID.randomUUID();
        final String password = encoder.encode(dto.rawPassword());

        final UserEntity user = UserEntity.byRegister(userId, password, discordId);
        final UserEntity created = repository.save(user);

        return new User(
            created.getUserId(),
            created.getDiscordId(),
            List.of(),
            List.of(),
            WellWishSpreadStatus.empty(),
            0
        );
    }

    public User login(LoginUser request) {
        UserEntity entity = repository.findByDiscordId(request.discordId())
                .orElseThrow(() -> new RuntimeException("wrong id or password")); //TODO

        String password = entity.getEncodedPassword();
        boolean isMatch = encoder.matches(request.rawPassword(), password);

        if(!isMatch)
            throw new RuntimeException("wrong id or password"); //TODO

        return aggregateToUser(entity);
    }

    private User aggregateToUser(UserEntity entity) {
        final List<WellWish> receivedWllWishes = wellWishRepository.findAllById(
                entity.getReceivedWelWishIds()
        ).stream().map(WellWishEntity::toDomain).toList();

        final List<WellWish> sentWellWishes = wellWishRepository.findAllById(
                entity.getSentWelWishIds()
        ).stream().map(WellWishEntity::toDomain).toList();

        final WellWishSpreadStatus spreadStatus = new WellWishSpreadStatus(
                entity.getTotalSpreadCount(),
                entity.getSpreadCountPerDegree()
        );

        return new User(
                entity.getUserId(),
                entity.getDiscordId(),
                receivedWllWishes,
                sentWellWishes,
                spreadStatus,
                entity.getKeyCount()
        );
    }
}
