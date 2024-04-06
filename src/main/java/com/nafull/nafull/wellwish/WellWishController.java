package com.nafull.nafull.wellwish;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.wellwish.data.ReceiveWellWish;
import com.nafull.nafull.wellwish.data.SendWellWish;
import com.nafull.nafull.wellwish.data.WellWish;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/well-wishes")
public class WellWishController {
    @GetMapping("/{wellWishId}")
    public WellWish findOne(
        @PathVariable final String wellWishId
    ) {
        throw new RuntimeException("Not Implemented!");
    }

    @PostMapping("/receive")
    public void receive(
        @RequestBody final ReceiveWellWish request
    ) {
        throw new RuntimeException("Not Implemented!");
    }

    @PostMapping("/send")
    public void send(
        @RequestBody final ListData<SendWellWish> request
    ) {
        throw new RuntimeException("Not Implemented!");
    }

    @PatchMapping("/{wellWishId}/unlock")
    public void unlock(
        @PathVariable final String wellWishId
    ) {
        throw new RuntimeException("Not Implemented!");
    }
}
