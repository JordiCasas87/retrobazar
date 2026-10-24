package com.retrobazar.user.application.port.out;

import com.retrobazar.user.domain.User;

/**
 * Apuntes del programador (A.P.):
 * Este puerto define las operaciones de persistencia que necesita la capa application,
 * sin depender de JPA, MySQL ni de otras tecnologías de la capa infrastructure.
 */
public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    User save(User user);
}
