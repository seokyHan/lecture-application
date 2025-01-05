package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.domain.lecture.repository.LectureRepository;
import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureQueryServiceUnitTest {

    @InjectMocks
    private LectureQueryService lectureQueryService;

    @Mock
    private LectureRepository lectureRepository;

    @DisplayName("id를 통해 강의를 조회한다.")
    @Test
    void getLectureByIdTest() {
        // given
        Long lectureId = 1L;
        Lecture lecture = new Lecture(lectureId, "title", "description", 2L, LocalDateTime.now(), null);
        when(lectureRepository.getLectureById(lectureId)).thenReturn(lecture);

        // when
        Lecture result = lectureQueryService.getLectureById(lectureId);

        // then
        assertEquals(result.id(), lectureId);
        assertEquals(result.title(), lecture.title());
        assertEquals(result.description(), lecture.description());
        assertEquals(result.lecturerId(), lecture.lecturerId());
        assertEquals(result.createdAt(), lecture.createdAt());
        assertEquals(result.updatedAt(), lecture.updatedAt());
    }

    @DisplayName("id를 통해 강의 스케줄을 조회한다.")
    @Test
    void getLectureScheduleById() {
        // given
        Long lectureScheduleId = 1L;
        LectureSchedule lectureSchedule = new LectureSchedule(lectureScheduleId, 1L, 30, 0, LocalDateTime.now(), LocalDateTime.now(), null, null);
        when(lectureRepository.getLectureScheduleById(lectureScheduleId, false)).thenReturn(lectureSchedule);

        // when
        LectureSchedule result = lectureQueryService.getLectureScheduleById(lectureScheduleId, false);

        // then
        assertEquals(result.id(), lectureScheduleId);
        assertEquals(result.lectureId(), lectureSchedule.lectureId());
        assertEquals(result.capacity(), lectureSchedule.capacity());
        assertEquals(result.enrolledCount(), lectureSchedule.enrolledCount());
        assertEquals(result.startAt(), lectureSchedule.startAt());
        assertEquals(result.endAt(), lectureSchedule.endAt());
    }

    @DisplayName("날짜를 통해 특강 스케줄을 조회한다.")
    @Test
    void findAvailableLectureSchedulesTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LectureSchedule lectureSchedule = new LectureSchedule(1L, 1L, 30, 0, now, now, null, null);
        when(lectureRepository.findAvailableLectureSchedulesByDateTime(now.toLocalDate())).thenReturn(List.of(lectureSchedule));

        // when
        List<LectureSchedule> lectureSchedules = lectureQueryService.findAvailableLectureSchedules(now.toLocalDate());

        // then
        assertThat(lectureSchedules).hasSize(1)
                .extracting("id", "lectureId", "capacity", "enrolledCount", "startAt", "endAt")
                .containsExactlyInAnyOrder(
                        tuple(lectureSchedules.get(0).id(),
                                lectureSchedules.get(0).lectureId(),
                                lectureSchedules.get(0).capacity(),
                                lectureSchedules.get(0).enrolledCount(),
                                lectureSchedules.get(0).startAt(),
                                lectureSchedules.get(0).endAt()
                        )
                );
    }

    @DisplayName("id를 통해 특강신청 목록을 조회한다.")
    @Test
    void getLectureEnrollmentByIdTest() {
        // given
        Long lectureEnrollmentId = 1L;
        LectureEnrollment lectureEnrollment = new LectureEnrollment(lectureEnrollmentId, 1L, 2L, 3L, LocalDateTime.now(), null);
        when(lectureRepository.getLectureEnrollmentById(lectureEnrollmentId)).thenReturn(lectureEnrollment);

        // when
        LectureEnrollment result = lectureQueryService.getLectureEnrollmentById(lectureEnrollmentId);

        // then
        assertEquals(result.id(), lectureEnrollmentId);
        assertEquals(result.userId(), lectureEnrollment.userId());
        assertEquals(result.lectureId(), lectureEnrollment.lectureId());
        assertEquals(result.lectureScheduleId(), lectureEnrollment.lectureScheduleId());
        assertEquals(result.createdAt(), lectureEnrollment.createdAt());
        assertEquals(result.updatedAt(), lectureEnrollment.updatedAt());
    }

    @DisplayName("userId를 통해 특강 신청이력들을 조회한다.")
    @Test
    void findLectureEnrollmentsTest() {
        // given
        Long userId = 1L;
        LectureEnrollment lectureEnrollment = new LectureEnrollment(1L, 1L, 2L, userId, LocalDateTime.now(), null);
        when(lectureRepository.findLectureEnrollmentsByUserId(userId)).thenReturn(List.of(lectureEnrollment));

        // when
        final List<LectureEnrollment> lectureEnrollments = lectureQueryService.findLectureEnrollments(userId);

        // then
        assertThat(lectureEnrollments).hasSize(1)
                .extracting("id", "lectureId", "userId", "createdAt")
                .containsExactlyInAnyOrder(
                        tuple(lectureEnrollments.get(0).id(),
                                lectureEnrollments.get(0).lectureId(),
                                lectureEnrollments.get(0).userId(),
                                lectureEnrollments.get(0).createdAt()
                        )
                );
    }

    @DisplayName("lectureIds 통해 특강을 조회한다.")
    @Test
    void findLecturesByIdsTest() {
        // given
        List<Long> lectureIds = List.of(1L, 2L);
        List<Lecture> lectures = List.of(
                new Lecture(1L, "title1", "description1", 1L, LocalDateTime.now(), null),
                new Lecture(2L, "title2", "description2", 2L, LocalDateTime.now(), null)
        );
        when(lectureRepository.findLecturesByIds(lectureIds)).thenReturn(lectures);

        // when
        List<Lecture> result = lectureQueryService.findLecturesByIds(lectureIds);

        // then
        assertThat(result).hasSize(2)
                .extracting("id", "title", "description", "lecturerId", "createdAt")
                .containsExactlyInAnyOrder(
                        tuple(result.get(0).id(),
                                result.get(0).title(),
                                result.get(0).description(),
                                result.get(0).lecturerId(),
                                result.get(0).createdAt()
                        ),
                        tuple(result.get(1).id(),
                                result.get(1).title(),
                                result.get(1).description(),
                                result.get(1).lecturerId(),
                                result.get(1).createdAt()
                        )
                );
    }

}