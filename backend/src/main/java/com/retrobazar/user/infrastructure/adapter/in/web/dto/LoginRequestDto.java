package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.application.command.LoginUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(max = 72) String password
) {

    public LoginUserCommand toCommand() {
        return new LoginUserCommand(email, password);
    }
}
