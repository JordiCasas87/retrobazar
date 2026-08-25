package com.retrobazar.infrastructure.web.error;

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
