package com.retrobazar.user.infrastructure.adapter.in.web.dto;

import com.retrobazar.user.application.command.RegisterUserCommand;
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

class RegisterUserRequestDtoValidationTest {

    private static final ValidatorFactory VALIDATOR_FACTORY =
            Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    void shouldAcceptValidRequestAndConvertItToCommand() {
        RegisterUserRequestDto request = new RegisterUserRequestDto(
                "Jordi Casas",
                "jordi@example.com",
                "Banana123"
        );

        Set<ConstraintViolation<RegisterUserRequestDto>> violations =
                VALIDATOR.validate(request);
        RegisterUserCommand command = request.toCommand();

        assertTrue(violations.isEmpty());
        assertEquals("Jordi Casas", command.name());
        assertEquals("jordi@example.com", command.email());
        assertEquals("Banana123", command.password());
    }

    @Test
    void shouldRejectBlankFieldsAndInvalidEmail() {
        RegisterUserRequestDto request = new RegisterUserRequestDto(
                " ",
                "invalid-email",
                " "
        );

        assertViolation(request, "name", NotBlank.class);
        assertViolation(request, "email", Email.class);
        assertViolation(request, "password", NotBlank.class);
    }

    @Test
    void shouldRejectValuesOutsideAllowedLengths() {
        RegisterUserRequestDto request = new RegisterUserRequestDto(
                "n".repeat(256),
                "e".repeat(244) + "@example.com",
                "p".repeat(73)
        );

        assertViolation(request, "name", Size.class);
        assertViolation(request, "email", Size.class);
        assertViolation(request, "password", Size.class);
        assertViolation(
                new RegisterUserRequestDto(
                        "Jordi Casas",
                        "jordi@example.com",
                        "short"
                ),
                "password",
                Size.class
        );
    }

    private void assertViolation(
            RegisterUserRequestDto request,
            String property,
            Class<?> annotationType
    ) {
        Set<ConstraintViolation<RegisterUserRequestDto>> violations =
                VALIDATOR.validate(request);

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
