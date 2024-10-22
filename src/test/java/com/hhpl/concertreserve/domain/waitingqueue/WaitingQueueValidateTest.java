package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hhpl.concertreserve.domain.error.ErrorType.*;
import static org.junit.jupiter.api.Assertions.*;

class WaitingQueueValidateTest {

    private final WaitingQueueValidator validator = new WaitingQueueValidator();

    @Test
    @DisplayName("UUID가 정규식을 통과하면 검증이 성공한다.")
    public void shouldPassValidationForValidUuid() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        assertDoesNotThrow(() -> validator.validateUserUuid(uuid));
    }

    @Test
    @DisplayName("UUID가 없으면 예외가 발생한다.")
    public void shouldThrowExceptionWhenUuidIsNull() {
        String uuid = "";
        CoreException exception = assertThrows(CoreException.class, () -> validator.validateUserUuid(uuid));

        assertEquals(UUID_MISSING, exception.getErrorType());
    }

    @Test
    @DisplayName("UUID가 형식에 맞지 않으면 예외가 발생한다.")
    public void shouldThrowExceptionWhenUuidFormatIsInvalid() {
        String uuid = "user123";
        CoreException exception = assertThrows(CoreException.class, () -> validator.validateUserUuid(uuid));

        assertEquals(UUID_VALIDATION_FAILED, exception.getErrorType());
    }

    @Test
    @DisplayName("유효한 concertId이면 검증이 성공한다.")
    public void shouldPassValidationForValidConcertId() {
        Long concertId = 1L;
        assertDoesNotThrow(() -> validator.validateConcertId(concertId));
    }

    @Test
    @DisplayName("concertId가 null이면 예외가 발생한다.")
    public void shouldThrowExceptionWhenConcertIdIsNull() {
        Long concertId = null;
        CoreException exception = assertThrows(CoreException.class, () -> validator.validateConcertId(concertId));

        assertEquals(CONCERT_ID_VALIDATION_FAILED, exception.getErrorType());
    }

    @Test
    @DisplayName("concertId가 0 이하이면 예외가 발생한다.")
    public void shouldThrowExceptionWhenConcertIdIsZeroOrLess() {
        Long concertId = -1L;
        CoreException exception = assertThrows(CoreException.class, () -> validator.validateConcertId(concertId));

        assertEquals(CONCERT_ID_VALIDATION_FAILED, exception.getErrorType());
    }

}