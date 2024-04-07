package com.nafull.nafull.letter;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.letter.data.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/letters")
@RequiredArgsConstructor
public class LetterController {
    private final LetterService letterService;

    @GetMapping("/{letterId}")
    public Letter findOne(
        @PathVariable final UUID letterId
    ) {
        return letterService.findOne(letterId);
    }

    @PostMapping("/receive")
    public void receive(
        @RequestBody final ReceiveLetter request
    ) {
        letterService.receive(request);
    }

    @PostMapping("/send")
    public ListData<Letter> send(
        @RequestBody final ListData<SendLetter> request
    ) {
        return letterService.send(request);
    }
    @PostMapping("/send/random")
    public ListData<Letter> sendRandom(
        @RequestBody final ListData<SendLetterRandom> request
    ) {
        return letterService.sendRandom(request);
    }

    @PatchMapping("/{letterId}/unlock")
    public void unlock(
        @PathVariable final UUID letterId,
        @RequestBody final UnlockLetter request
    ) {
        letterService.unlock(letterId, request.userId());
    }
}
