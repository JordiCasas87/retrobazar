package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        UserRole role
) {

    public static UserResponseDto fromUser(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
