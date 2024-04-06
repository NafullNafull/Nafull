package com.nafull.nafull.user;

import com.nafull.nafull.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultUserCreator implements ApplicationRunner {
    private final DefaultUser defaultUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        Optional<UserEntity> defaultUserOrNull = userRepository.findById(defaultUser.getId());

        if(defaultUserOrNull.isPresent()) {
            if(!Objects.equals(defaultUserOrNull.get().getDiscordId(), defaultUser.getDiscordId()))
                throw new RuntimeException(
                    String.format(
                        "default user discord id is diffrent! expect: %s, actual: %s",
                        defaultUser.getDiscordId(),
                        defaultUserOrNull.get().getDiscordId()
                    ));
            else return;
        }
        if(userRepository.findByDiscordId(defaultUser.getDiscordId()).isPresent())
            throw new RuntimeException("default user is already registered to another account!");

        UserEntity entity = new UserEntity(
            defaultUser.getId(),
            passwordEncoder.encode(defaultUser.getRawPassword()),
            defaultUser.getDiscordId(),
            0
        );
        userRepository.save(entity);
    }
}
