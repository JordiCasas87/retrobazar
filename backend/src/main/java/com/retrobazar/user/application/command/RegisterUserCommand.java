package com.retrobazar.user.application.command;

public record RegisterUserCommand(
        String name,
        String email,
        String password
) {
}
