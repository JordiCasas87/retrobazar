package com.retrobazar.user.infrastructure.adapter.in.web.dto;

public record LoginResponseDto(
        String accessToken,
        String tokenType
) {

    public static LoginResponseDto bearer(String accessToken) {
        return new LoginResponseDto(accessToken, "Bearer");
    }
}
