package com.retrobazar.user.infrastructure.adapter.in.web;

import com.retrobazar.user.application.command.LoginUserCommand;
import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.application.port.in.LoginUserUseCase;
import com.retrobazar.user.application.port.in.RegisterUserUseCase;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.LoginRequestDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.LoginResponseDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.RegisterUserRequestDto;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthControllerTest {

    @Test
    void shouldRegisterUserAndReturnCreatedResponseWithoutPassword() {
        UUID userId = UUID.fromString("2e1e71a3-7861-434c-9d72-82d91e652694");
        User registeredUser = new User(
                userId,
                "Jordi Casas",
                "jordi@example.com",
                "hashed-password",
                UserRole.CUSTOMER,
                true
        );
        FakeRegisterUserUseCase registerUserUseCase =
                new FakeRegisterUserUseCase(registeredUser);
        AuthController controller = new AuthController(
                registerUserUseCase,
                command -> "unused-token"
        );
        RegisterUserRequestDto request = new RegisterUserRequestDto(
                "Jordi Casas",
                "jordi@example.com",
                "Banana123",
                true
        );

        ResponseEntity<UserResponseDto> response = controller.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().id());
        assertEquals("Jordi Casas", response.getBody().name());
        assertEquals("jordi@example.com", response.getBody().email());
        assertEquals(UserRole.CUSTOMER, response.getBody().role());
        assertTrue(response.getBody().newProductEmailNotificationsEnabled());

        assertNotNull(registerUserUseCase.receivedCommand);
        assertEquals("Jordi Casas", registerUserUseCase.receivedCommand.name());
        assertEquals("jordi@example.com", registerUserUseCase.receivedCommand.email());
        assertEquals("Banana123", registerUserUseCase.receivedCommand.password());
        assertTrue(registerUserUseCase.receivedCommand.newProductEmailNotificationsEnabled());

        List<String> responseFields = Arrays.stream(UserResponseDto.class.getRecordComponents())
                .map(component -> component.getName())
                .toList();
        assertEquals(
                List.of(
                        "id",
                        "name",
                        "email",
                        "role",
                        "newProductEmailNotificationsEnabled"
                ),
                responseFields
        );
    }

    @Test
    void shouldLoginUserAndReturnBearerToken() {
        FakeLoginUserUseCase loginUserUseCase =
                new FakeLoginUserUseCase("generated-jwt");
        AuthController controller = new AuthController(
                command -> null,
                loginUserUseCase
        );
        LoginRequestDto request = new LoginRequestDto(
                "jordi@example.com",
                "Banana123"
        );

        ResponseEntity<LoginResponseDto> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("generated-jwt", response.getBody().accessToken());
        assertEquals("Bearer", response.getBody().tokenType());
        assertNotNull(loginUserUseCase.receivedCommand);
        assertEquals("jordi@example.com", loginUserUseCase.receivedCommand.email());
        assertEquals("Banana123", loginUserUseCase.receivedCommand.password());
    }

    private static final class FakeRegisterUserUseCase implements RegisterUserUseCase {

        private final User userToReturn;
        private RegisterUserCommand receivedCommand;

        private FakeRegisterUserUseCase(User userToReturn) {
            this.userToReturn = userToReturn;
        }

        @Override
        public User registerUser(RegisterUserCommand command) {
            receivedCommand = command;
            return userToReturn;
        }
    }

    private static final class FakeLoginUserUseCase implements LoginUserUseCase {

        private final String tokenToReturn;
        private LoginUserCommand receivedCommand;

        private FakeLoginUserUseCase(String tokenToReturn) {
            this.tokenToReturn = tokenToReturn;
        }

        @Override
        public String login(LoginUserCommand command) {
            receivedCommand = command;
            return tokenToReturn;
        }
    }
}
