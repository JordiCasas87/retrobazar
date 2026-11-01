package com.retrobazar.user.infrastructure.adapter.out.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BCryptPasswordEncoderAdapterTest {

    private final BCryptPasswordEncoderAdapter passwordEncoder =
            new BCryptPasswordEncoderAdapter();

    @Test
    void shouldEncodeAndMatchPassword() {
        String rawPassword = "Banana123";

        String passwordHash = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, passwordHash);
        assertTrue(passwordEncoder.matches(rawPassword, passwordHash));
        assertFalse(passwordEncoder.matches("wrong-password", passwordHash));
    }
}
