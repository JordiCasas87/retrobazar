package com.retrobazar.user.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    private static final UUID USER_ID =
            UUID.fromString("862b27ae-a936-4af1-8f69-b87669a40926");

    @Test
    void shouldCreateAValidUser() {
        User user = new User(
                USER_ID,
                "Jordi Casas",
                "jordi@example.com",
                "encoded-password",
                UserRole.CUSTOMER
        );

        assertAll(
                () -> assertEquals(USER_ID, user.getId()),
                () -> assertEquals("Jordi Casas", user.getName()),
                () -> assertEquals("jordi@example.com", user.getEmail()),
                () -> assertEquals("encoded-password", user.getPasswordHash()),
                () -> assertEquals(UserRole.CUSTOMER, user.getRole())
        );
    }

    @Test
    void shouldRejectMissingRequiredData() {
        assertAll(
                () -> assertThrows(
                        NullPointerException.class,
                        () -> createUser(null, "Jordi", "jordi@example.com", "hash", UserRole.CUSTOMER)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> createUser(USER_ID, " ", "jordi@example.com", "hash", UserRole.CUSTOMER)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> createUser(USER_ID, "Jordi", " ", "hash", UserRole.CUSTOMER)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> createUser(USER_ID, "Jordi", "jordi@example.com", " ", UserRole.CUSTOMER)
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> createUser(USER_ID, "Jordi", "jordi@example.com", "hash", null)
                )
        );
    }

    private static User createUser(
            UUID id,
            String name,
            String email,
            String passwordHash,
            UserRole role
    ) {
        return new User(id, name, email, passwordHash, role);
    }
}
