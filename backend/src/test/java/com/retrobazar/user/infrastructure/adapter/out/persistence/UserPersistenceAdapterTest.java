package com.retrobazar.user.infrastructure.adapter.out.persistence;

import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserPersistenceAdapter.class)
@Testcontainers
class UserPersistenceAdapterTest {

    @Container
    @ServiceConnection
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer(DockerImageName.parse("mysql:latest"));

    @Autowired
    private UserPersistenceAdapter userPersistenceAdapter;

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    @Test
    void shouldSaveAndReturnUser() {
        User user = createUser("jordi@example.com");

        User savedUser = userPersistenceAdapter.save(user);
        UserJpaEntity storedUser = springDataUserRepository.findById(user.getId())
                .orElseThrow();

        assertEquals(user.getId(), savedUser.getId());
        assertEquals("Jordi Casas", savedUser.getName());
        assertEquals("jordi@example.com", savedUser.getEmail());
        assertEquals("hashed-password", savedUser.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, savedUser.getRole());

        assertEquals(user.getId(), storedUser.getId());
        assertEquals("Jordi Casas", storedUser.getName());
        assertEquals("jordi@example.com", storedUser.getEmail());
        assertEquals("hashed-password", storedUser.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, storedUser.getRole());
    }

    @Test
    void shouldDetectWhetherEmailExists() {
        userPersistenceAdapter.save(createUser("jordi@example.com"));

        assertTrue(userPersistenceAdapter.existsByEmail("jordi@example.com"));
        assertFalse(userPersistenceAdapter.existsByEmail("other@example.com"));
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        userPersistenceAdapter.save(createUser("jordi@example.com"));
        userPersistenceAdapter.save(createUser("jordi@example.com"));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> springDataUserRepository.flush()
        );
    }

    private User createUser(String email) {
        return new User(
                UUID.randomUUID(),
                "Jordi Casas",
                email,
                "hashed-password",
                UserRole.CUSTOMER
        );
    }
}
