package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.domain.ProductCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRequestDtoValidationTest {

    private static final ValidatorFactory VALIDATOR_FACTORY =
            Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    void shouldValidateRequiredFieldsInCreateRequest() {
        CreateProductRequestDto request = new CreateProductRequestDto(
                null, null, null, null, null, null, null, null
        );

        assertRequiredFieldViolations(request, true);
    }

    @Test
    void shouldValidateRequiredFieldsInUpdateRequest() {
        UpdateProductRequestDto request = new UpdateProductRequestDto(
                null, null, null, null, null, null, null
        );

        assertRequiredFieldViolations(request, false);
    }

    @Test
    void shouldValidateLimitsAndFormatsInCreateRequest() {
        CreateProductRequestDto request = new CreateProductRequestDto(
                " ",
                "b".repeat(256),
                "d".repeat(2001),
                new BigDecimal("123456789.123"),
                -1,
                ProductCategory.GADGETS,
                List.of(
                        " ",
                        "ftp://example.com/image.jpg",
                        "https://example.com/" + "x".repeat(1000)
                ),
                true
        );

        assertLimitAndFormatViolations(request);
    }

    @Test
    void shouldValidateLimitsAndFormatsInUpdateRequest() {
        UpdateProductRequestDto request = new UpdateProductRequestDto(
                " ",
                "b".repeat(256),
                "d".repeat(2001),
                new BigDecimal("123456789.123"),
                -1,
                ProductCategory.GADGETS,
                List.of(
                        " ",
                        "ftp://example.com/image.jpg",
                        "https://example.com/" + "x".repeat(1000)
                )
        );

        assertLimitAndFormatViolations(request);
    }

    @Test
    void shouldValidatePriceAndImageCollectionBoundsInBothRequests() {
        List<String> tooManyImages = List.of(
                "https://example.com/1", "https://example.com/2",
                "https://example.com/3", "https://example.com/4",
                "https://example.com/5", "https://example.com/6"
        );
        CreateProductRequestDto createRequest = validCreateRequest(BigDecimal.ZERO, tooManyImages);
        UpdateProductRequestDto updateRequest = validUpdateRequest(BigDecimal.ZERO, tooManyImages);

        assertViolation(createRequest, "price", Positive.class);
        assertViolation(createRequest, "imageUrls", Size.class);
        assertViolation(updateRequest, "price", Positive.class);
        assertViolation(updateRequest, "imageUrls", Size.class);

        assertViolation(validCreateRequest(BigDecimal.ONE, List.of()), "imageUrls", Size.class);
        assertViolation(validUpdateRequest(BigDecimal.ONE, List.of()), "imageUrls", Size.class);
    }

    @Test
    void shouldAcceptValidRequestsAndUnboxValuesInCommands() {
        CreateProductRequestDto createRequest = validCreateRequest(
                new BigDecimal("99999999.99"),
                List.of("https://example.com/image.jpg")
        );
        UpdateProductRequestDto updateRequest = validUpdateRequest(
                new BigDecimal("0.01"),
                List.of("http://example.com/image.jpg")
        );

        assertTrue(VALIDATOR.validate(createRequest).isEmpty());
        assertTrue(VALIDATOR.validate(updateRequest).isEmpty());
        assertEquals(0, createRequest.toCommand().stock());
        assertTrue(createRequest.toCommand().active());
        assertEquals(0, updateRequest.toCommand().stock());
    }

    private void assertRequiredFieldViolations(Object request, boolean includesActive) {
        assertViolation(request, "name", NotBlank.class);
        assertViolation(request, "brand", NotBlank.class);
        assertViolation(request, "description", NotBlank.class);
        assertViolation(request, "price", NotNull.class);
        assertViolation(request, "stock", NotNull.class);
        assertViolation(request, "category", NotNull.class);
        assertViolation(request, "imageUrls", NotNull.class);
        if (includesActive) {
            assertViolation(request, "active", NotNull.class);
        }
    }

    private void assertLimitAndFormatViolations(Object request) {
        assertViolation(request, "name", NotBlank.class);
        assertViolation(request, "brand", Size.class);
        assertViolation(request, "description", Size.class);
        assertViolation(request, "price", Digits.class);
        assertViolation(request, "stock", PositiveOrZero.class);
        assertViolation(request, "imageUrls[0].<list element>", NotBlank.class);
        assertViolation(request, "imageUrls[1].<list element>", Pattern.class);
        assertViolation(request, "imageUrls[2].<list element>", Size.class);
    }

    private void assertViolation(
            Object request,
            String property,
            Class<?> annotationType
    ) {
        Set<? extends ConstraintViolation<?>> violations = VALIDATOR.validate(request);

        assertTrue(
                violations.stream().anyMatch(violation ->
                        violation.getPropertyPath().toString().equals(property)
                                && violation.getConstraintDescriptor()
                                .getAnnotation().annotationType().equals(annotationType)
                ),
                () -> "Expected " + annotationType.getSimpleName() + " violation for " + property
                        + ", but got " + violations
        );
    }

    private CreateProductRequestDto validCreateRequest(
            BigDecimal price,
            List<String> imageUrls
    ) {
        return new CreateProductRequestDto(
                "Pixel Clock", "Divoom", "A pixel art clock",
                price, 0, ProductCategory.GADGETS, imageUrls, true
        );
    }

    private UpdateProductRequestDto validUpdateRequest(
            BigDecimal price,
            List<String> imageUrls
    ) {
        return new UpdateProductRequestDto(
                "Pixel Clock", "Divoom", "A pixel art clock",
                price, 0, ProductCategory.GADGETS, imageUrls
        );
    }
}
