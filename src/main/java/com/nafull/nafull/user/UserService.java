package com.nafull.nafull.user;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.common.ListUtil;
import com.nafull.nafull.common.error.ErrorCode;
import com.nafull.nafull.common.error.WebException;
import com.nafull.nafull.statistic.data.UserStatistic;
import com.nafull.nafull.user.data.LoginUser;
import com.nafull.nafull.user.data.RegisterUser;
import com.nafull.nafull.user.data.User;
import com.nafull.nafull.user.data.UserRelation;
import com.nafull.nafull.user.entity.UserEntity;
import com.nafull.nafull.user.entity.UserRelationEntity;
import com.nafull.nafull.letter.LetterRepository;
import com.nafull.nafull.letter.data.Letter;
import com.nafull.nafull.letter.entity.LetterEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final LetterRepository letterRepository;

    public User findOneByDiscordId(String discordId) {
        final UserEntity entity = userRepository.findByDiscordId(discordId)
            .orElseThrow(() -> new WebException("유저를 찾을 수 없어요!", ErrorCode.USER_NOT_FOUND));

        return aggregateToUser(entity);
    }

    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new WebException("유저를 찾을 수 없어요!", ErrorCode.USER_NOT_FOUND));
    }

    public Long countAll() {
        return userRepository.count();
    }

    public ListData<String> findAllDiscordIds() {
        final List<UserEntity> users = userRepository.findAll();
        final List<String> discordIds = users.stream().map(UserEntity::getDiscordId).toList();
        return new ListData<>(discordIds);
    }

    @Transactional
    public User register(RegisterUser dto) {
        final LetterEntity letter =
            letterRepository.findById(dto.letterId())
                .orElseThrow(() -> new WebException("마음편지를 찾을 수 없어요", ErrorCode.LETTER_NOT_FOUND));

        final String discordId = letter.getReceiverDiscordId();

        final boolean alreadyRegisteredDiscordId = userRepository.findByDiscordId(discordId).isPresent();
        if(alreadyRegisteredDiscordId)
            throw new WebException("이미 회원가입된 디스코드 ID에요", ErrorCode.ALREADY_REGISTERED);

        final UUID userId = UUID.randomUUID();
        final String password = encoder.encode(dto.rawPassword());

        final UserEntity user = UserEntity.byRegister(userId, dto.nickname(), password, discordId);

        final UUID spreaderId = letter.getSenderId();
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
            created.getNickname(),
            List.of(),
            List.of(),
            0L,
            created.getWingCount(),
            created.getRegistrationTimestamp()
        );
    }

    public User login(LoginUser dto) {
        final UserEntity entity = userRepository.findByDiscordId(dto.discordId())
            .orElseThrow(() -> new WebException("ID 또는 비밀번호가 잘못되었어요", ErrorCode.WRONG_ID_OR_PASSWORD));

        final String password = entity.getEncodedPassword();
        final boolean isPasswordMatch = encoder.matches(dto.rawPassword(), password);

        if(!isPasswordMatch)
            throw new WebException("ID 또는 비밀번호가 잘못되었어요", ErrorCode.WRONG_ID_OR_PASSWORD);

        return aggregateToUser(entity);
    }

    public UserStatistic calculateStatistics(UUID userId) {
        Long registrationOrder = calculateUserRegistrationOrder(userId);
        Long totalSpreadCount = calculateUserTotalSpreadCount(userId);

        return new UserStatistic(
                totalSpreadCount,
                registrationOrder
        );
    }

    private Long calculateUserRegistrationOrder(UUID userId) {
        //FIXME Performance Issue!
        List<UserEntity> allUsers = userRepository.findAll(Sort.by(Sort.Direction.ASC, "registrationTimestamp"));
        long registrationOrder = -1L;
        for (int i = 0; i < allUsers.size(); i++) {
            if(allUsers.get(i).getUserId().equals(userId)) {
                registrationOrder = i;
                break;
            }
        }
        if(registrationOrder == -1) throw new WebException("유저를 찾을 수 없어요!", ErrorCode.USER_NOT_FOUND);
        return registrationOrder;
    }

    public Long calculateUserTotalSpreadCount(UUID userId) {
        final List<UserRelationEntity> list = userRelationRepository.findAllByRelateUserId(userId);
        return list.stream()
            .map(UserRelationEntity::getUserId)
            .distinct()
            .filter(id -> id != userId)
            .count();
    }

    @Transactional
    public void plusWings(UUID userId, Integer countToAdd) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new WebException("유저를 찾을 수 없어요!", ErrorCode.USER_NOT_FOUND));
        entity.addWings(countToAdd);
        userRepository.save(entity);
    }

    @Transactional
    public void minusWings(UUID userId, Integer countToMinus) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new WebException("유저를 찾을 수 없어요!", ErrorCode.USER_NOT_FOUND));
        entity.minusWings(countToMinus);
        userRepository.save(entity);
    }

    private UserRelation findUserRelation(UUID userId) {
        final List<UserRelationEntity> entities = userRelationRepository.findAllByUserId(userId);
        final List<UUID> relateUserIds = entities.stream().map(UserRelationEntity::getRelateUserId).toList();
        return new UserRelation(userId, relateUserIds);
    }

    private User aggregateToUser(UserEntity entity) {
        final List<Letter> receivedWllWishes = letterRepository.findAllByReceiverDiscordId(
            entity.getDiscordId()
        ).stream().map(LetterEntity::toDomainWithContentLock).toList();

        final List<Letter> sentLetters = letterRepository.findAllBySenderId(
            entity.getUserId()
        ).stream().map(LetterEntity::toDomainWithContentLock).toList();

        return new User(
            entity.getUserId(),
            entity.getDiscordId(),
            entity.getNickname(),
            receivedWllWishes,
            sentLetters,
            calculateUserTotalSpreadCount(entity.getUserId()),
            entity.getWingCount(),
            entity.getRegistrationTimestamp()
        );
    }
}
