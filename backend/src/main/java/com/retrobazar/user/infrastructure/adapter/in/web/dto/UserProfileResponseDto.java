package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.domain.User;

public record UserProfileResponseDto(
        String name,
        String email
) {

    public static UserProfileResponseDto fromUser(User user) {
        return new UserProfileResponseDto(user.getName(), user.getEmail());
    }
}
