package com.retrobazar.user.application.port.in;

import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.domain.User;

public interface RegisterUserUseCase {

    User registerUser(RegisterUserCommand command);
}
