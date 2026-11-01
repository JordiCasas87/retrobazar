package com.retrobazar.user.application.command;

public record LoginUserCommand(
        String email,
        String password
) {
}
