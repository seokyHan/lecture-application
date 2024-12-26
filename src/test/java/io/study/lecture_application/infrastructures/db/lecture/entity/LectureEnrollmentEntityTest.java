package io.study.lecture_application.infrastructures.db.lecture.entity;

import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LectureEnrollmentEntityTest {

    @DisplayName("LectureEnrollmentEntity를 LectureEnrollment Model로 반환한다.")
    @Test
    void LectureEnrollmentEntityConvertToLectureEnrollmentModelTest() {
        //given
        LectureEnrollmentEntity lectureEnrollmentEntity = LectureEnrollmentEntity.builder()
                .id(1L)
                .lectureId(2L)
                .lectureScheduleId(3L)
                .userId(4L)
                .createdAt(LocalDateTime.now())
                .build();

        //when
        LectureEnrollment lectureEnrollment = lectureEnrollmentEntity.toLectureEnrollmentDomain();

        //then
        assertEquals(lectureEnrollmentEntity.getId(), lectureEnrollment.id());
        assertEquals(lectureEnrollmentEntity.getLectureId(), lectureEnrollment.lectureId());
        assertEquals(lectureEnrollmentEntity.getLectureScheduleId(), lectureEnrollment.lectureScheduleId());
        assertEquals(lectureEnrollmentEntity.getUserId(), lectureEnrollment.userId());
        assertEquals(lectureEnrollmentEntity.getCreatedAt(), lectureEnrollment.createdAt());
    }


}