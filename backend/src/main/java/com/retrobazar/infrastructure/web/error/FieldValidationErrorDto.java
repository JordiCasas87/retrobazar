package com.retrobazar.infrastructure.web.error;

public record FieldValidationErrorDto(
        String field,
        String message
) {
}
