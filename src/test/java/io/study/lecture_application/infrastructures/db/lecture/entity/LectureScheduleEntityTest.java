package io.study.lecture_application.infrastructures.db.lecture.entity;

import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LectureScheduleEntityTest {


    @DisplayName("LectureScheduleEntity 객체를 LectureSchedule Model로 반환한다.")
    @Test
    void LectureScheduleEntityConvertToLectureScheduleModelTest() {
        //given
        LectureScheduleEntity lectureScheduleEntity = LectureScheduleEntity.builder()
                .id(1L)
                .lectureId(2L)
                .capacity(30)
                .enrolledCount(10)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusDays(1L))
                .build();
        //when
        LectureSchedule lectureSchedule = lectureScheduleEntity.toLectureScheduleDomain();

        //then
        assertEquals(lectureScheduleEntity.getId(), lectureSchedule.id());
        assertEquals(lectureScheduleEntity.getLectureId(), lectureSchedule.lectureId());
        assertEquals(lectureScheduleEntity.getCapacity(), lectureSchedule.capacity());
        assertEquals(lectureScheduleEntity.getEnrolledCount(), lectureSchedule.enrolledCount());
        assertEquals(lectureScheduleEntity.getStartAt(), lectureSchedule.startAt());
        assertEquals(lectureScheduleEntity.getEndAt(), lectureSchedule.endAt());
    }

    @Test
    @DisplayName("increaseEnrollmentCount 테스트 성공")
    void shouldSuccessfullyIncreaseEnrollmentCount() {
        // given
        LectureScheduleEntity lectureScheduleEntity = new LectureScheduleEntity(1L, 1L, 30, 0, LocalDateTime.now(), LocalDateTime.now(), null, null);

        // when
        lectureScheduleEntity.increaseEnrollmentCount();

        // then
        assertThat(lectureScheduleEntity.getEnrolledCount()).isEqualTo(1);
    }

}