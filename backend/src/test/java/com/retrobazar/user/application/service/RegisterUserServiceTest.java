package com.retrobazar.user.application.service;

import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.application.exception.EmailAlreadyRegisteredException;
import com.retrobazar.user.application.port.out.PasswordEncoderPort;
import com.retrobazar.user.application.port.out.UserRepositoryPort;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterUserServiceTest {

    @Test
    void shouldRegisterACustomerWithNormalizedEmailAndEncodedPassword() {
        FakeUserRepository userRepository = new FakeUserRepository(false);
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder("encoded-password");
        RegisterUserService service = new RegisterUserService(
                userRepository,
                passwordEncoder
        );
        RegisterUserCommand command = new RegisterUserCommand(
                "Jordi Casas",
                "  Jordi@Example.COM ",
                "plain-password"
        );

        User result = service.registerUser(command);

        assertEquals("jordi@example.com", userRepository.checkedEmail);
        assertEquals("plain-password", passwordEncoder.receivedPassword);
        assertNotNull(result.getId());
        assertEquals("Jordi Casas", result.getName());
        assertEquals("jordi@example.com", result.getEmail());
        assertEquals("encoded-password", result.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, result.getRole());
        assertSame(userRepository.savedUser, result);
    }

    @Test
    void shouldRejectAnAlreadyRegisteredEmailWithoutEncodingOrSaving() {
        FakeUserRepository userRepository = new FakeUserRepository(true);
        FakePasswordEncoder passwordEncoder = new FakePasswordEncoder("encoded-password");
        RegisterUserService service = new RegisterUserService(
                userRepository,
                passwordEncoder
        );
        RegisterUserCommand command = new RegisterUserCommand(
                "Jordi Casas",
                "jordi@example.com",
                "plain-password"
        );

        assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> service.registerUser(command)
        );

        assertEquals("jordi@example.com", userRepository.checkedEmail);
        assertNull(passwordEncoder.receivedPassword);
        assertNull(userRepository.savedUser);
    }

    // Clases creadas para hacer fake en los tests de esta clase; no usamos Mockito.
    private static final class FakeUserRepository implements UserRepositoryPort {

        private final boolean emailExists;
        private String checkedEmail;
        private User savedUser;

        private FakeUserRepository(boolean emailExists) {
            this.emailExists = emailExists;
        }

        @Override
        public boolean existsByEmail(String email) {
            checkedEmail = email;
            return emailExists;
        }

        @Override
        public User save(User user) {
            savedUser = user;
            return user;
        }
    }

    private static final class FakePasswordEncoder implements PasswordEncoderPort {

        private final String encodedPassword;
        private String receivedPassword;

        private FakePasswordEncoder(String encodedPassword) {
            this.encodedPassword = encodedPassword;
        }

        @Override
        public String encode(String rawPassword) {
            receivedPassword = rawPassword;
            return encodedPassword;
        }
    }
}
