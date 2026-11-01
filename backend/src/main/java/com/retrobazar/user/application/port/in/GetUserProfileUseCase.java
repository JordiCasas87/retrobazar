package com.retrobazar.user.application.port.in;

import com.retrobazar.user.domain.User;

import java.util.UUID;

public interface GetUserProfileUseCase {

    User getProfile(UUID userId);
}
