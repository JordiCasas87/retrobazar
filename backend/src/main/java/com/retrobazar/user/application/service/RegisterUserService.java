package com.retrobazar.user.application.service;

import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.application.exception.EmailAlreadyRegisteredException;
import com.retrobazar.user.application.port.in.RegisterUserUseCase;
import com.retrobazar.user.application.port.out.PasswordEncoderPort;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegisterUserService(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoderPort passwordEncoderPort
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public User registerUser(RegisterUserCommand command) {
        String normalizedEmail = command.email().trim().toLowerCase(Locale.ROOT);

        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException(normalizedEmail);
        }

        String passwordHash = passwordEncoderPort.encode(command.password());
        User user = new User(
                UUID.randomUUID(),
                command.name(),
                normalizedEmail,
                passwordHash,
                UserRole.CUSTOMER
        );

        return userRepositoryPort.save(user);
    }
}
