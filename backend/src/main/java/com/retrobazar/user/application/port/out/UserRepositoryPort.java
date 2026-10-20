package com.retrobazar.user.application.port.out;

import com.retrobazar.user.domain.User;

public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    User save(User user);
}
