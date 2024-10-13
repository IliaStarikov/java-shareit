package ru.practicum.shareit.booking;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingSearchStatusTest {

    @ParameterizedTest(name = "{0} should have the name {1}")
    @CsvSource({
            "ALL, ALL",
            "CURRENT, CURRENT",
            "PAST, PAST",
            "FUTURE, FUTURE",
            "WAITING, WAITING",
            "REJECTED, REJECTED"
    })
    void checkingStates_HasToHaveTheRightNames(BookingSearchStatus status, String expectedName) {
        // Assert: Проверяем, что имя каждого состояния соответствует ожидаемому значению
        assertEquals(expectedName, status.name());
    }

    @ParameterizedTest(name = "{0} should have the ordinal value {1}")
    @CsvSource({
            "ALL, 0",
            "CURRENT, 1",
            "PAST, 2",
            "FUTURE, 3",
            "WAITING, 4",
            "REJECTED, 5"
    })
    void checkingOrdinalValues_HasLongToHaveTheRightSerialNumbers(BookingSearchStatus status, int expectedOrdinal) {
        // Assert: Проверяем, что порядковый номер каждого состояния совпадает с ожидаемым
        assertEquals(expectedOrdinal, status.ordinal());
    }
}