package com.nafull.nafull.letter;

import com.nafull.nafull.letter.entity.LetterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LetterRepository extends JpaRepository<LetterEntity, UUID> {
    List<LetterEntity> findAllByReceiverDiscordId(String receiverDiscordId);
    List<LetterEntity> findAllBySenderId(UUID senderId);
}
