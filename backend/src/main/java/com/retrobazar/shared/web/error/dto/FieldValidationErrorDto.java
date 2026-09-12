package com.retrobazar.shared.web.error.dto;

public record FieldValidationErrorDto(
        String field,
        String message
) {
}
