package com.retrobazar.user.application.service;

import com.retrobazar.user.application.exception.UserNotFoundException;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetUserProfileServiceTest {

    @Test
    void shouldReturnTheUserProfileForItsId() {
        User user = createUser();
        FakeUserRepository userRepository = new FakeUserRepository(user);
        GetUserProfileService service = new GetUserProfileService(userRepository);

        User result = service.getProfile(user.getId());

        assertEquals(user.getId(), userRepository.receivedId);
        assertSame(user, result);
    }

    @Test
    void shouldRejectAnUnknownUserId() {
        UUID userId = UUID.randomUUID();
        FakeUserRepository userRepository = new FakeUserRepository(null);
        GetUserProfileService service = new GetUserProfileService(userRepository);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> service.getProfile(userId)
        );

        assertEquals(userId, userRepository.receivedId);
        assertEquals("User not found: " + userId, exception.getMessage());
    }

    private static User createUser() {
        return new User(
                UUID.randomUUID(),
                "Jordi Casas",
                "jordi@example.com",
                "encoded-password",
                UserRole.CUSTOMER,
                true
        );
    }

    private static final class FakeUserRepository implements UserRepositoryPort {

        private final User user;
        private UUID receivedId;

        private FakeUserRepository(User user) {
            this.user = user;
        }

        @Override
        public boolean existsByEmail(String email) {
            return false;
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public Optional<User> findById(UUID id) {
            receivedId = id;
            return Optional.ofNullable(user);
        }

        @Override
        public User save(User user) {
            return user;
        }
    }
}
