package com.retrobazar.shared.web.error.dto;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponseDto(
        int status,
        String code,
        String message,
        String path,
        Instant timestamp,
        List<FieldValidationErrorDto> fieldErrors
) {
}
