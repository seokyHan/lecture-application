package io.study.lecture_application.common.validator;

import io.study.lecture_application.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LectureValidatorTest {

    @DisplayName("특강 생성시 id값이 null이 아니거나 0보다 크다면 예외가 발생하지 않는다.")
    @Test
    public void createLectureEnrollmentValidateValidInputTest() {
        assertDoesNotThrow(() -> LectureValidator.createLectureEnrollmentValidate(1L, 1L, 1L));
    }

    @DisplayName("lectureId 값이 0보다 작거나 같거나 null 이라면 예외가 발생한다.")
    @Test
    public void createLectureEnrollmentValidateInvalidLectureIdTest() {
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(null, 1L, 1L));
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(0L, 1L, 1L));
    }

    @DisplayName("lectureScheduleId 값이 0보다 작거나 같거나 null 이라면 예외가 발생한다.")
    @Test
    public void createLectureEnrollmentValidateInvalidLectureScheduleIdTest() {
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(1L, null, 1L));
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(1L, 0L, 1L));
    }

    @DisplayName("userId 값이 0보다 작거나 같거나 null 이라면 예외가 발생한다.")
    @Test
    public void createLectureEnrollmentValidateInvalidUserIdTest() {
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(1L, 1L, null));
        assertThrows(CustomException.class, () -> LectureValidator.createLectureEnrollmentValidate(1L, 1L, 0L));
    }

    @DisplayName("lectureId 값이 0보다 크고 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void getLectureByIdValidateValidInputTest() {
        assertDoesNotThrow(() -> LectureValidator.getLectureByIdValidate(1L));
    }

    @DisplayName("lectureId 값이 0보다 작거나 같거나 null 이라면 예외가 발생한다.")
    @Test
    public void getLectureByIdValidateInvalidInputTest() {
        assertThrows(CustomException.class, () -> LectureValidator.getLectureByIdValidate(null));
        assertThrows(CustomException.class, () -> LectureValidator.getLectureByIdValidate(0L));
    }

    @DisplayName("lectureScheduleId 값이 0보다 크고 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void getLectureScheduleByIdValidateValidInputTest() {
        assertDoesNotThrow(() -> LectureValidator.getLectureScheduleByIdValidate(1L));
    }

    @DisplayName("lectureScheduleId 값이 0보다 작거나 null 이라면 예외가 발생한다.")
    @Test
    public void getLectureScheduleByIdValidateInvalidInputTest() {
        assertThrows(CustomException.class, () -> LectureValidator.getLectureScheduleByIdValidate(null));
        assertThrows(CustomException.class, () -> LectureValidator.getLectureScheduleByIdValidate(0L));
    }

    @DisplayName("date 값이 현재보다 이전 날짜가 아니거나 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void availableLectureSchedulesValidateValidInputTest() {
        assertDoesNotThrow(() -> LectureValidator.availableLectureSchedulesValidate(LocalDate.now().plusDays(1)));
    }

    @DisplayName("date 값이 현재보다 이전 날짜거나 null 이라면 예외가 발생한다.")
    @Test
    public void availableLectureSchedulesValidateInvalidInputTest() {
        assertThrows(CustomException.class, () -> LectureValidator.availableLectureSchedulesValidate(null));
        assertThrows(CustomException.class, () -> LectureValidator.availableLectureSchedulesValidate(LocalDate.now().minusDays(1)));
    }

    @DisplayName("lectureEnrollmentId 값이 0보다 크거나 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void getLectureEnrollmentByIdValidateValidInputTest() {
        assertDoesNotThrow(() -> LectureValidator.getLectureEnrollmentByIdValidate(1L));
    }

    @DisplayName("lectureEnrollmentId 값이 0보다 작거나 null 이라면 예외가 발생한다.")
    @Test
    public void getLectureEnrollmentByIdValidateInvalidInputTest() {
        assertThrows(CustomException.class, () -> LectureValidator.getLectureEnrollmentByIdValidate(null));
        assertThrows(CustomException.class, () -> LectureValidator.getLectureEnrollmentByIdValidate(0L));
    }

    @DisplayName("userId 값이 0보다 크거나 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void testGetLectureEnrollmentsByUserId_ValidInput() {
        assertDoesNotThrow(() -> LectureValidator.getLectureEnrollmentsByUserId(1L));
    }

    @DisplayName("userId 값이 0보다 작거나 null 이라면 예외가 발생한다.")
    @Test
    public void testGetLectureEnrollmentsByUserId_InvalidInput() {
        assertThrows(CustomException.class, () -> LectureValidator.getLectureEnrollmentsByUserId(null));
        assertThrows(CustomException.class, () -> LectureValidator.getLectureEnrollmentsByUserId(0L));
    }

    @DisplayName("lectureIds 값들이 0보다 크거나, null이 아니거나 또는 List가 비어있지 않거나 null이 아니라면 예외가 발생하지 않는다.")
    @Test
    public void testGetLecturesByIds_ValidInput() {
        assertDoesNotThrow(() -> LectureValidator.getLecturesByIds(Arrays.asList(1L, 2L, 3L)));
    }

    @DisplayName("lectureIds 값들이 0보다 작거나, null 이거나 또는 List가 비어있거나 null 이라면 예외가 발생한다.")
    @Test
    public void testGetLecturesByIds_InvalidInput() {
        assertThrows(CustomException.class, () -> LectureValidator.getLecturesByIds(null));
        assertThrows(CustomException.class, () -> LectureValidator.getLecturesByIds(Arrays.asList()));
        assertThrows(CustomException.class, () -> LectureValidator.getLecturesByIds(Arrays.asList(1L, null, 3L)));
        assertThrows(CustomException.class, () -> LectureValidator.getLecturesByIds(Arrays.asList(1L, 0L, 3L)));
    }

}