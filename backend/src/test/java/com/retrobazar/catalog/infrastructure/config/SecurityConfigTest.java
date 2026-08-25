package com.retrobazar.catalog.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    @Test
    void shouldAllowConfiguredFrontendForApiRequests() {
        SecurityConfig securityConfig = new SecurityConfig();
        CorsConfigurationSource source = securityConfig.corsConfigurationSource(
                "http://localhost:4200"
        );
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/products");

        CorsConfiguration configuration = source.getCorsConfiguration(request);

        assertNotNull(configuration);
        assertEquals(
                List.of("http://localhost:4200"),
                configuration.getAllowedOrigins()
        );
        assertEquals(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"),
                configuration.getAllowedMethods()
        );
        assertEquals(List.of("*"), configuration.getAllowedHeaders());
    }
}
