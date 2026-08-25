package com.retrobazar.infrastructure.web.error;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private static final String REQUEST_PATH = "/api/admin/products";

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest(
            "POST",
            REQUEST_PATH
    );

    @Test
    void shouldReturnValidationErrors() throws NoSuchMethodException {
        ValidationTarget target = new ValidationTarget();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "request");
        bindingResult.rejectValue("name", "NotBlank", "must not be blank");
        Method method = ValidationTarget.class.getDeclaredMethod("accept", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ApiErrorResponseDto> response =
                handler.handleValidationException(exception, request);

        assertResponse(response, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
        assertEquals(
                new FieldValidationErrorDto("name", "must not be blank"),
                response.getBody().fieldErrors().getFirst()
        );
    }

    @Test
    void shouldReturnProductNotFoundError() {
        UUID productId = UUID.fromString("f7e017c5-8f43-493c-a88b-8a603b2cae52");

        ResponseEntity<ApiErrorResponseDto> response =
                handler.handleProductNotFoundException(
                        new ProductNotFoundException(productId),
                        request
                );

        assertResponse(response, HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND");
        assertEquals("Product not found: " + productId, response.getBody().message());
    }

    @Test
    void shouldReturnMalformedRequestWithoutExposingInternalDetails() {
        HttpInputMessage inputMessage = new MockHttpInputMessage(new byte[0]);
        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException("Sensitive parser detail", inputMessage);

        ResponseEntity<ApiErrorResponseDto> response =
                handler.handleUnreadableMessageException(exception, request);

        assertResponse(response, HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST");
        assertEquals("Request body is missing or malformed", response.getBody().message());
    }

    @Test
    void shouldReturnInvalidArgumentError() {
        ResponseEntity<ApiErrorResponseDto> response =
                handler.handleIllegalArgumentException(
                        new IllegalArgumentException("price must be greater than zero"),
                        request
                );

        assertResponse(response, HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT");
        assertEquals("price must be greater than zero", response.getBody().message());
    }

    @Test
    void shouldReturnInternalErrorWithoutExposingInternalDetails() {
        ResponseEntity<ApiErrorResponseDto> response =
                handler.handleUnexpectedException(
                        new RuntimeException("Sensitive internal detail"),
                        request
                );

        assertResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR");
        assertEquals("An unexpected error occurred", response.getBody().message());
    }

    private void assertResponse(
            ResponseEntity<ApiErrorResponseDto> response,
            HttpStatus expectedStatus,
            String expectedCode
    ) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedStatus.value(), response.getBody().status());
        assertEquals(expectedCode, response.getBody().code());
        assertEquals(REQUEST_PATH, response.getBody().path());
        assertNotNull(response.getBody().timestamp());
        assertNotNull(response.getBody().fieldErrors());
    }

    private static class ValidationTarget {

        @SuppressWarnings("unused")
        void accept(String name) {
        }

        @SuppressWarnings("unused")
        public String getName() {
            return null;
        }
    }
}
