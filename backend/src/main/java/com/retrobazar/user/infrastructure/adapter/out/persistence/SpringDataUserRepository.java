package com.retrobazar.user.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Apuntes del programador (A.P.):
 * Este repositorio conecta UserJpaEntity con Spring Data JPA y proporciona
 * las operaciones necesarias para consultar y guardar usuarios en la base de datos.
 */
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByEmail(String email);
}
