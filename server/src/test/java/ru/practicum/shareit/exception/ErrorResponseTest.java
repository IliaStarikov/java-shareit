package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void constructor_ShouldInitializeFields() {
        String expectedStatus = "404 Not Found";
        String expectedErrorMessage = "Entity not found";

        ErrorResponse errorResponse = new ErrorResponse(expectedStatus, expectedErrorMessage);

        assertThat(errorResponse.getStatus()).isEqualTo(expectedStatus);
        assertThat(errorResponse.getMessage()).isEqualTo(expectedErrorMessage);
    }
}
