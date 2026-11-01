package com.retrobazar.user.infrastructure.adapter.in.web;

import com.retrobazar.user.application.port.in.GetUserProfileUseCase;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import com.retrobazar.user.infrastructure.adapter.in.web.dto.UserProfileResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserProfileControllerTest {

    @Test
    void shouldGetTheAuthenticatedUserProfileWithoutExposingPrivateFields() {
        UUID userId = UUID.fromString("2e1e71a3-7861-434c-9d72-82d91e652694");
        User user = new User(
                userId,
                "Jordi Casas",
                "jordi@example.com",
                "hashed-password",
                UserRole.CUSTOMER,
                true
        );
        FakeGetUserProfileUseCase useCase = new FakeGetUserProfileUseCase(user);
        UserProfileController controller = new UserProfileController(useCase);
        Jwt jwt = createJwt(userId);

        UserProfileResponseDto response = controller.getProfile(jwt);

        assertEquals(userId, useCase.receivedUserId);
        assertEquals("Jordi Casas", response.name());
        assertEquals("jordi@example.com", response.email());

        List<String> responseFields = Arrays.stream(
                        UserProfileResponseDto.class.getRecordComponents()
                )
                .map(component -> component.getName())
                .toList();
        assertEquals(List.of("name", "email"), responseFields);
    }

    private Jwt createJwt(UUID userId) {
        Instant issuedAt = Instant.parse("2026-11-01T10:00:00Z");
        return Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .subject(userId.toString())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(3600))
                .build();
    }

    private static final class FakeGetUserProfileUseCase
            implements GetUserProfileUseCase {

        private final User userToReturn;
        private UUID receivedUserId;

        private FakeGetUserProfileUseCase(User userToReturn) {
            this.userToReturn = userToReturn;
        }

        @Override
        public User getProfile(UUID userId) {
            receivedUserId = userId;
            return userToReturn;
        }
    }
}
