package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.application.command.RegisterUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDto(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 72) String password
) {

    public RegisterUserCommand toCommand() {
        return new RegisterUserCommand(name, email, password);
    }
}
