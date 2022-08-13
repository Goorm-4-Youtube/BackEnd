package com.goorm.team4.youtubeclone.Controller;

import com.goorm.team4.youtubeclone.Service.UserRegistrationService;
import com.goorm.team4.youtubeclone.Service.UserService;
import com.goorm.team4.youtubeclone.Service.VideoService;
import com.goorm.team4.youtubeclone.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final VideoService videoService;
    private final UserRegistrationService userRegistrationService;

    @GetMapping("/register")
    public List<String> register(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return userRegistrationService.registerUser(jwt.getTokenValue());
    }

    @PostMapping("subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String userId) {
        userService.subscribeUser(userId);
        return true;
    }

    @PostMapping("unSubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unSubscribeUser(@PathVariable String userId) {
        userService.unSubscribeUser(userId);
        return true;
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable String userId) {
        return userService.userHistory(userId);
    }


    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getHistory() {
        return videoService.getVideoList(userService.getHistory());
    }

    @GetMapping("/likedVideo")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getLikedVideo() {
        return videoService.getVideoList(userService.getLikedVideo());

    }

    @GetMapping("/dislikedVideo")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getDisLikedVideo() {
        return videoService.getdisLikeVideoList(userService.getdisLikedVideo());

    }
}