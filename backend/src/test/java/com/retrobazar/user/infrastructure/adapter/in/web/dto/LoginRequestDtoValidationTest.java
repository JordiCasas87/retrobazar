package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.application.command.LoginUserCommand;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestDtoValidationTest {

    private static final ValidatorFactory VALIDATOR_FACTORY =
            Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    void shouldAcceptValidRequestAndConvertItToCommand() {
        LoginRequestDto request = new LoginRequestDto(
                "jordi@example.com",
                "Banana123"
        );

        Set<ConstraintViolation<LoginRequestDto>> violations = VALIDATOR.validate(request);
        LoginUserCommand command = request.toCommand();

        assertTrue(violations.isEmpty());
        assertEquals("jordi@example.com", command.email());
        assertEquals("Banana123", command.password());
    }

    @Test
    void shouldRejectInvalidEmailAndBlankPassword() {
        LoginRequestDto request = new LoginRequestDto("invalid-email", " ");

        assertViolation(request, "email", Email.class);
        assertViolation(request, "password", NotBlank.class);
    }

    @Test
    void shouldRejectValuesOutsideAllowedLengths() {
        LoginRequestDto request = new LoginRequestDto(
                "e".repeat(244) + "@example.com",
                "p".repeat(73)
        );

        assertViolation(request, "email", Size.class);
        assertViolation(request, "password", Size.class);
    }

    private void assertViolation(
            LoginRequestDto request,
            String property,
            Class<?> annotationType
    ) {
        Set<ConstraintViolation<LoginRequestDto>> violations = VALIDATOR.validate(request);

        assertTrue(
                violations.stream().anyMatch(violation ->
                        violation.getPropertyPath().toString().equals(property)
                                && violation.getConstraintDescriptor()
                                .getAnnotation().annotationType().equals(annotationType)
                ),
                () -> "Expected " + annotationType.getSimpleName()
                        + " violation for " + property + ", but got " + violations
        );
    }
}
