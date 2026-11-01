package com.retrobazar.user.application.service;

import com.retrobazar.user.application.command.LoginUserCommand;
import com.retrobazar.user.application.exception.InvalidCredentialsException;
import com.retrobazar.user.application.port.in.LoginUserUseCase;
import com.retrobazar.user.application.port.out.PasswordEncoderPort;
import com.retrobazar.user.application.port.out.TokenProviderPort;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LoginUserService implements LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public LoginUserService(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public String login(LoginUserCommand command) {
        String normalizedEmail = command.email().trim().toLowerCase(Locale.ROOT);
        User user = userRepositoryPort.findByEmail(normalizedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoderPort.matches(command.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return tokenProviderPort.generateToken(user);
    }
}
