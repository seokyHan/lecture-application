package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.lecture.dto.LectureInfo;
import io.study.lecture_application.domain.lecture.repository.LectureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static io.study.lecture_application.common.code.LectureErrorCode.DUPLICATE_ENROLLMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceUnitTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureRepository lectureRepository;

    @DisplayName("이미 특강 신청이 되어있는 경우 예외가 발생 한다.")
    @Test
    void shouldThrowExceptionWhenDuplicateEnrollment() {
        //given
        LectureInfo.CreateEnrollmentLecture lectureInfo = new LectureInfo.CreateEnrollmentLecture(1L, 1L, 1L);
        when(lectureRepository.createLectureEnrollment(lectureInfo.lectureId(), lectureInfo.lectureScheduleId(), lectureInfo.userId())).thenThrow(new DataIntegrityViolationException(""));

        //when & then
        assertThatThrownBy(() -> lectureService.createLectureEnrollment(lectureInfo))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_ENROLLMENT.getMessage());
    }

    @DisplayName("lectureId, lectureScheduleId, userId를 입력 받아 특강을 신청한다.")
    @Test
    void createLectureEnrollmentTest() {
        //given
        Long lectureEnrollmentId = 1L;
        LectureInfo.CreateEnrollmentLecture lectureInfo = new LectureInfo.CreateEnrollmentLecture(1L, 1L, 1L);
        when(lectureRepository.createLectureEnrollment(lectureInfo.lectureId(), lectureInfo.lectureScheduleId(), lectureInfo.userId())).thenReturn(lectureEnrollmentId);

        // when
        final Long result = lectureService.createLectureEnrollment(lectureInfo);

        // then
        assertThat(result).isEqualTo(lectureEnrollmentId);
    }



}