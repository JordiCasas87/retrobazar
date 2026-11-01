package com.retrobazar.user.application.service;

import com.retrobazar.user.application.exception.UserNotFoundException;
import com.retrobazar.user.application.port.in.GetUserProfileUseCase;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetUserProfileService implements GetUserProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserProfileService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User getProfile(UUID userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
