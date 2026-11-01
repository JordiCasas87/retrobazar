package com.retrobazar.user.application.port.in;

import com.retrobazar.user.application.command.LoginUserCommand;

public interface LoginUserUseCase {

    String login(LoginUserCommand command);
}
