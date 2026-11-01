package com.retrobazar.user.infrastructure.adapter.out.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.retrobazar.user.application.port.out.TokenProviderPort;
import com.retrobazar.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private static final int MINIMUM_SECRET_LENGTH = 32;

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;
    private final Clock clock;

    @Autowired
    public JwtTokenProviderAdapter(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration}") Duration expiration
    ) {
        this(createJwtEncoder(secret), expiration, Clock.systemUTC());
    }

    JwtTokenProviderAdapter(JwtEncoder jwtEncoder, Duration expiration, Clock clock) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
        this.clock = clock;
    }

    @Override
    public String generateToken(User user) {
        Instant issuedAt = clock.instant();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://retrobazar.local")
                .subject(user.getId().toString())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(expiration))
                .claim("role", user.getRole().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }

    private static JwtEncoder createJwtEncoder(String secret) {
        if (secret.length() < MINIMUM_SECRET_LENGTH) {
            throw new IllegalArgumentException("JWT secret must contain at least 32 characters");
        }

        return new NimbusJwtEncoder(
                new ImmutableSecret<>(secret.getBytes(StandardCharsets.UTF_8))
        );
    }
}
