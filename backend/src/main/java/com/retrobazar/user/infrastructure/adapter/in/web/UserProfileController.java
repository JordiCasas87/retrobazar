package com.retrobazar.user.infrastructure.adapter.in.web;

import com.retrobazar.user.application.port.in.GetUserProfileUseCase;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.UserProfileResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/me")
public class UserProfileController {

    private final GetUserProfileUseCase getUserProfileUseCase;

    public UserProfileController(GetUserProfileUseCase getUserProfileUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
    }

    @GetMapping
    public UserProfileResponseDto getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        User user = getUserProfileUseCase.getProfile(userId);
        return UserProfileResponseDto.fromUser(user);
    }
}
