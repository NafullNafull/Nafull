package com.nafull.nafull.wellwish;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.wellwish.data.ReceiveWellWish;
import com.nafull.nafull.wellwish.data.SendWellWish;
import com.nafull.nafull.wellwish.data.UnlockWellWish;
import com.nafull.nafull.wellwish.data.WellWish;
import com.nafull.nafull.wellwish.entity.WellWishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/well-wishes")
@RequiredArgsConstructor
public class WellWishController {
    private final WellWishService wellWishService;

    @GetMapping("/{wellWishId}")
    public WellWish findOne(
        @PathVariable final UUID wellWishId
    ) {
        return wellWishService.findOne(wellWishId);
    }

    @PostMapping("/receive")
    public void receive(
        @RequestBody final ReceiveWellWish request
    ) {
        wellWishService.receive(request);
    }

    @PostMapping("/send")
    public void send(
        @RequestBody final ListData<SendWellWish> request
    ) {
        wellWishService.send(request);
    }

    @PatchMapping("/{wellWishId}/unlock")
    public void unlock(
        @PathVariable final UUID wellWishId,
        @RequestBody final UnlockWellWish request
    ) {
        wellWishService.unlock(wellWishId, request.userId());
    }
}
