package com.nafull.nafull.user;

import com.nafull.nafull.common.ListData;
import com.nafull.nafull.user.data.RegisterUser;
import com.nafull.nafull.user.data.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping("/{userId}")
    public User findOne(
        @PathVariable final String userId
    ) {
        throw new RuntimeException("Not Implemented!");
    }

    @GetMapping("/all/discord-ids")
    public ListData<String> findAllDiscordIds() {
        throw new RuntimeException("Not Implemented!");
    }

    @PostMapping("/register")
    public User register(
        @RequestBody RegisterUser request
    ) {
        throw new RuntimeException("Not Implemented!");
    }

    @PostMapping("/login")
    public User login(
        @RequestBody LoginUser request
    ) {
        return userService.login(request);
    }
}
