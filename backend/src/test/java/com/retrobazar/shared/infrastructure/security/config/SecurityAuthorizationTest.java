package com.retrobazar.shared.infrastructure.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityAuthorizationTest.TestController.class)
@Import({SecurityConfig.class, SecurityAuthorizationTest.TestController.class})
@TestPropertySource(properties = {
        "app.cors.allowed-origin=http://localhost:4200",
        "app.security.jwt.secret=a-secure-test-secret-with-at-least-32-characters"
})
class SecurityAuthorizationTest {

    private static final String SECRET =
            "a-secure-test-secret-with-at-least-32-characters";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowPublicCatalogAndAuthenticationEndpointsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectProtectedUserEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users/me/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAuthenticatedCustomerOnUserEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/me/profile")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("CUSTOMER")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectCustomerOnAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("CUSTOMER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminOnAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/products")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldProtectEndpointsWithoutAnExplicitPublicRule() throws Exception {
        mockMvc.perform(get("/api/other"))
                .andExpect(status().isUnauthorized());
    }

    private String bearerToken(String role) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://retrobazar.local")
                .subject("2e1e71a3-7861-434c-9d72-82d91e652694")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("role", role)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(SECRET.getBytes(StandardCharsets.UTF_8))
        );
        String token = encoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return "Bearer " + token;
    }

    @RestController
    public static class TestController {

        @GetMapping("/api/products")
        String products() {
            return "products";
        }

        @PostMapping("/api/auth/login")
        String login() {
            return "token";
        }

        @GetMapping("/api/users/me/profile")
        String profile() {
            return "profile";
        }

        @GetMapping("/api/admin/products")
        String adminProducts() {
            return "admin-products";
        }

        @GetMapping("/api/other")
        String other() {
            return "other";
        }
    }
}
