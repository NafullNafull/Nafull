package com.nafull.nafull.user;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.user.data.RegisterUser;
import com.nafull.nafull.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/discord-id/{discordId}")
    public User findOneByDiscordId(
        @PathVariable final String discordId
    ) {
        return userService.findOneByDiscordId(discordId);
    }

    @GetMapping("/all/discord-ids")
    public ListData<String> findAllDiscordIds() {
        return userService.findAllDiscordIds();
    }

    @PostMapping("/register")
    public User register(
        @RequestBody RegisterUser request
    ) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public User login(
        @RequestBody LoginUser request
    ) {
        return userService.login(request);
    }
}
