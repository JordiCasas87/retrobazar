package com.retrobazar.user.application.service;

import com.retrobazar.user.application.command.LoginUserCommand;
import com.retrobazar.user.application.exception.InvalidCredentialsException;
import com.retrobazar.user.application.port.out.PasswordEncoderPort;
import com.retrobazar.user.application.port.out.TokenProviderPort;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginUserServiceTest {

    @Test
    void shouldReturnATokenWhenCredentialsAreValid() {
        User user = createUser();
        FakeUserRepository userRepository = new FakeUserRepository(user);
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder(true);
        FakeTokenProvider tokenProvider = new FakeTokenProvider("generated-token");
        LoginUserService service = new LoginUserService(
                userRepository,
                passwordEncoder,
                tokenProvider
        );

        String result = service.login(new LoginUserCommand(
                "  Jordi@Example.COM ",
                "plain-password"
        ));

        assertEquals("jordi@example.com", userRepository.receivedEmail);
        assertEquals("plain-password", passwordEncoder.receivedRawPassword);
        assertEquals("encoded-password", passwordEncoder.receivedPasswordHash);
        assertEquals(user, tokenProvider.receivedUser);
        assertEquals("generated-token", result);
    }

    @Test
    void shouldRejectAnUnknownEmailWithoutCheckingThePassword() {
        FakeUserRepository userRepository = new FakeUserRepository(null);
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder(true);
        FakeTokenProvider tokenProvider = new FakeTokenProvider("generated-token");
        LoginUserService service = new LoginUserService(
                userRepository,
                passwordEncoder,
                tokenProvider
        );

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(new LoginUserCommand("unknown@example.com", "plain-password"))
        );

        assertNull(passwordEncoder.receivedRawPassword);
        assertNull(tokenProvider.receivedUser);
    }

    @Test
    void shouldRejectAnIncorrectPasswordWithoutGeneratingAToken() {
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder(false);
        FakeTokenProvider tokenProvider = new FakeTokenProvider("generated-token");
        LoginUserService service = new LoginUserService(
                new FakeUserRepository(createUser()),
                passwordEncoder,
                tokenProvider
        );

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(new LoginUserCommand("jordi@example.com", "wrong-password"))
        );

        assertEquals("wrong-password", passwordEncoder.receivedRawPassword);
        assertNull(tokenProvider.receivedUser);
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
        private String receivedEmail;

        private FakeUserRepository(User user) {
            this.user = user;
        }

        @Override
        public boolean existsByEmail(String email) {
            return false;
        }

        @Override
        public Optional<User> findByEmail(String email) {
            receivedEmail = email;
            return Optional.ofNullable(user);
        }

        @Override
        public User save(User user) {
            return user;
        }
    }

    private static final class FakePasswordEncoder implements PasswordEncoderPort {

        private final boolean passwordsMatch;
        private String receivedRawPassword;
        private String receivedPasswordHash;

        private FakePasswordEncoder(boolean passwordsMatch) {
            this.passwordsMatch = passwordsMatch;
        }

        @Override
        public String encode(String rawPassword) {
            return rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String passwordHash) {
            receivedRawPassword = rawPassword;
            receivedPasswordHash = passwordHash;
            return passwordsMatch;
        }
    }

    private static final class FakeTokenProvider implements TokenProviderPort {

        private final String token;
        private User receivedUser;

        private FakeTokenProvider(String token) {
            this.token = token;
        }

        @Override
        public String generateToken(User user) {
            receivedUser = user;
            return token;
        }
    }
}
