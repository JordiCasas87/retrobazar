package com.retrobazar.user.application.port.out;

import com.retrobazar.user.domain.User;

public interface TokenProviderPort {

    String generateToken(User user);
}
