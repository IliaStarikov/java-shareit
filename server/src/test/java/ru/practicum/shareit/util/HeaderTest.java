package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

public class HeaderTest {

    @Test
    void testUserIdHeader() {
        // Assert: Проверка заголовка
        assertThat(X_SHARER_USER_ID).isEqualTo("X-Sharer-User-Id");
    }
}