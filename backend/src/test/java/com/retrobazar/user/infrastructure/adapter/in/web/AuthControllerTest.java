package com.retrobazar.user.infrastructure.adapter.in.web;

import com.retrobazar.user.application.command.RegisterUserCommand;
import com.retrobazar.user.application.port.in.RegisterUserUseCase;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
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

class AuthControllerTest {

    @Test
    void shouldRegisterUserAndReturnCreatedResponseWithoutPassword() {
        UUID userId = UUID.fromString("2e1e71a3-7861-434c-9d72-82d91e652694");
        User registeredUser = new User(
                userId,
                "Jordi Casas",
                "jordi@example.com",
                "hashed-password",
                UserRole.CUSTOMER
        );
        FakeRegisterUserUseCase registerUserUseCase =
                new FakeRegisterUserUseCase(registeredUser);
        AuthController controller = new AuthController(registerUserUseCase);
        RegisterUserRequestDto request = new RegisterUserRequestDto(
                "Jordi Casas",
                "jordi@example.com",
                "Banana123"
        );

        ResponseEntity<UserResponseDto> response = controller.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().id());
        assertEquals("Jordi Casas", response.getBody().name());
        assertEquals("jordi@example.com", response.getBody().email());
        assertEquals(UserRole.CUSTOMER, response.getBody().role());

        assertNotNull(registerUserUseCase.receivedCommand);
        assertEquals("Jordi Casas", registerUserUseCase.receivedCommand.name());
        assertEquals("jordi@example.com", registerUserUseCase.receivedCommand.email());
        assertEquals("Banana123", registerUserUseCase.receivedCommand.password());

        List<String> responseFields = Arrays.stream(UserResponseDto.class.getRecordComponents())
                .map(component -> component.getName())
                .toList();
        assertEquals(List.of("id", "name", "email", "role"), responseFields);
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
}
