package com.retrobazar.user.infrastructure.adapter.out.persistence;

import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Apuntes del programador (A.P.):
 * Este adaptador implementa el puerto de persistencia de la capa application.
 * Convierte entre User de la capa domain y UserJpaEntity de la capa infrastructure,
 * y delega las operaciones de base de datos en SpringDataUserRepository.
 */
@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    public UserPersistenceAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserJpaEntity userEntity = toEntity(user);
        UserJpaEntity savedUserEntity = springDataUserRepository.save(userEntity);
        return toDomain(savedUserEntity);
    }

    private UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole()
        );
    }

    private User toDomain(UserJpaEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPasswordHash(),
                userEntity.getRole()
        );
    }
}
