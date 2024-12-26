package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.lecture.dto.LectureInfo;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureEnrollmentJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureScheduleJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static io.study.lecture_application.common.code.LectureErrorCode.DUPLICATE_ENROLLMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;
    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;

    @AfterEach
    void setUp() {
        lectureScheduleJpaRepository.deleteAllInBatch();
        lectureEnrollmentJpaRepository.deleteAllInBatch();
    }


    @DisplayName("이미 특강 신청이 되어있는 경우 예외가 발생 한다.")
    @Test
    void shouldThrowExceptionWhenDuplicateEnrollment() {
        //given
        LectureInfo.CreateEnrollmentLecture lectureInfo = new LectureInfo.CreateEnrollmentLecture(99L, 99L, 99L);
        lectureService.createLectureEnrollment(lectureInfo);

        //when & then
        assertThatThrownBy(() -> lectureService.createLectureEnrollment(lectureInfo))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_ENROLLMENT.getMessage());
    }

    @Test
    @DisplayName("특강을 신청한다.")
    void createLectureEnrollmentTest() {
        // given
        LectureInfo.CreateEnrollmentLecture lectureInfo = new LectureInfo.CreateEnrollmentLecture(1L, 1L, 1L);

        // when
        Long result = lectureService.createLectureEnrollment(lectureInfo);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("현재 등록된 특강자 수를 증가시킨다.")
    void shouldSuccessfullyIncreaseEnrollmentCountByLectureScheduleId() {
        // given
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 1L, 30, 0, LocalDateTime.now(), LocalDateTime.now(), null, null));

        // when
        lectureService.increaseEnrollmentCountByLectureScheduleId(lectureScheduleEntity.getId());
        LectureScheduleEntity updatedLectureScheduleEntity = lectureScheduleJpaRepository.findById(lectureScheduleEntity.getId()).get();

        // then
        assertEquals(updatedLectureScheduleEntity.getEnrolledCount(),lectureScheduleEntity.getEnrolledCount() + 1);
    }


}