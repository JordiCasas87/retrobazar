package com.retrobazar.user.infrastructure.adapter.out.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.retrobazar.user.domain.User;
import com.retrobazar.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenProviderAdapterTest {

    private static final String SECRET = "a-secure-test-secret-with-at-least-32-characters";
    private static final Instant NOW = Instant.parse("2026-11-01T10:00:00Z");

    @Test
    void shouldBeCreatedBySpringWithConfiguredProperties() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext()) {
            TestPropertyValues.of(
                    "app.security.jwt.secret=" + SECRET,
                    "app.security.jwt.expiration=PT1H"
            ).applyTo(context);
            context.getBeanFactory().setConversionService(
                    ApplicationConversionService.getSharedInstance()
            );
            context.register(JwtTokenProviderAdapter.class);
            context.refresh();

            assertNotNull(context.getBean(JwtTokenProviderAdapter.class));
        }
    }

    @Test
    void shouldGenerateASignedTokenWithUserIdentityRoleAndExpiration() {
        Duration expiration = Duration.ofHours(1);
        JwtTokenProviderAdapter adapter = new JwtTokenProviderAdapter(
                new NimbusJwtEncoder(new ImmutableSecret<>(SECRET.getBytes(StandardCharsets.UTF_8))),
                expiration,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "Jordi Casas",
                "jordi@example.com",
                "encoded-password",
                UserRole.ADMIN,
                true
        );

        String token = adapter.generateToken(user);
        Jwt jwt = createDecoder().decode(token);

        assertEquals("https://retrobazar.local", jwt.getIssuer().toString());
        assertEquals(userId.toString(), jwt.getSubject());
        assertEquals("ADMIN", jwt.getClaimAsString("role"));
        assertEquals(NOW, jwt.getIssuedAt());
        assertEquals(NOW.plus(expiration), jwt.getExpiresAt());
    }

    private static NimbusJwtDecoder createDecoder() {
        SecretKey secretKey = new SecretKeySpec(
                SECRET.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(jwt -> OAuth2TokenValidatorResult.success());
        return decoder;
    }
}
