package com.retrobazar.user.application.port.out;

import com.retrobazar.user.domain.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Apuntes del programador (A.P.):
 * Este puerto define las operaciones de persistencia que necesita la capa application,
 * sin depender de JPA, MySQL ni de otras tecnologías de la capa infrastructure.
 */
public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    User save(User user);
}
