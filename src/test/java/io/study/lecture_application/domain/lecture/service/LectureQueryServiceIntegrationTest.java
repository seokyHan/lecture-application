package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEnrollmentEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureEnrollmentJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureScheduleJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureQueryServiceIntegrationTest {

    @Autowired
    private LectureQueryService lectureQueryService;
    @Autowired
    private LectureJpaRepository lectureJpaRepository;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;
    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;

    @BeforeEach
    void setUp() {
        lectureJpaRepository.deleteAll();
        lectureScheduleJpaRepository.deleteAll();
        lectureEnrollmentJpaRepository.deleteAll();
    }

    @DisplayName("id를 통해 강의를 조회한다.")
    @Test
    void getLectureByIdTest() {
        // given
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));

        // when
        Lecture result = lectureQueryService.getLectureById(lectureEntity.getId());

        // then
        assertEquals(result.id(), lectureEntity.getId());
        assertEquals(result.title(), lectureEntity.getTitle());
        assertEquals(result.description(), lectureEntity.getDescription());
        assertEquals(result.lecturerId(), lectureEntity.getLecturerId());
        assertEquals(result.createdAt(), lectureEntity.getCreatedAt());
        assertEquals(result.updatedAt(), lectureEntity.getUpdatedAt());
    }

    @DisplayName("id를 통해 강의 스케줄을 조회한다.")
    @Test
    void getLectureScheduleById() {
        // given
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 1L, 30, 0, LocalDateTime.now(), LocalDateTime.now(), null, null));

        // when
        LectureSchedule result = lectureQueryService.getLectureScheduleById(lectureScheduleEntity.getId(), false);

        // then
        assertEquals(result.id(), lectureScheduleEntity.getId());
        assertEquals(result.lectureId(), lectureScheduleEntity.getLectureId());
        assertEquals(result.capacity(), lectureScheduleEntity.getCapacity());
        assertEquals(result.enrolledCount(), lectureScheduleEntity.getEnrolledCount());
        assertEquals(result.startAt(), lectureScheduleEntity.getStartAt());
        assertEquals(result.endAt(), lectureScheduleEntity.getEndAt());
    }

    @DisplayName("날짜를 통해 특강 스케줄을 조회한다.")
    @Test
    void findAvailableLectureSchedulesTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        lectureScheduleJpaRepository.saveAll(List.of(
                new LectureScheduleEntity(null, 1L, 30, 0, now, now, null, null),
                new LectureScheduleEntity(null, 2L, 10, 5, now, now, null, null),
                new LectureScheduleEntity(null, 2L, 10, 10, now, now, null, null),
                new LectureScheduleEntity(null, 3L, 30, 0, now.minusDays(1), now.minusDays(1), null, null)
                )
        );

        // when
        List<LectureSchedule> lectureSchedules = lectureQueryService.findAvailableLectureSchedules(now.toLocalDate());

        // then
        assertThat(lectureSchedules).hasSize(2)
                .extracting("id", "lectureId", "capacity", "enrolledCount", "startAt", "endAt")
                .containsExactlyInAnyOrder(
                        tuple(lectureSchedules.get(0).id(),
                                lectureSchedules.get(0).lectureId(),
                                lectureSchedules.get(0).capacity(),
                                lectureSchedules.get(0).enrolledCount(),
                                lectureSchedules.get(0).startAt(),
                                lectureSchedules.get(0).endAt()
                        ),
                        tuple(lectureSchedules.get(1).id(),
                                lectureSchedules.get(1).lectureId(),
                                lectureSchedules.get(1).capacity(),
                                lectureSchedules.get(1).enrolledCount(),
                                lectureSchedules.get(1).startAt(),
                                lectureSchedules.get(1).endAt()
                        )
                );
    }

    @DisplayName("id를 통해 특강신청 목록을 조회한다.")
    @Test
    void getLectureEnrollmentByIdTest() {
        // given
        LectureEnrollmentEntity lectureEnrollmentEntity = lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(2L, 3L, 4L));

        // when
        LectureEnrollment result = lectureQueryService.getLectureEnrollmentById(lectureEnrollmentEntity.getId());

        // then
        assertEquals(result.id(), lectureEnrollmentEntity.getId());
        assertEquals(result.userId(), lectureEnrollmentEntity.getUserId());
        assertEquals(result.lectureId(), lectureEnrollmentEntity.getLectureId());
        assertEquals(result.lectureScheduleId(), lectureEnrollmentEntity.getLectureScheduleId());
        assertEquals(result.createdAt(), lectureEnrollmentEntity.getCreatedAt());
        assertEquals(result.updatedAt(), lectureEnrollmentEntity.getUpdatedAt());
    }

    @DisplayName("userId를 통해 특강 신청이력들을 조회한다.")
    @Test
    void findLectureEnrollmentsTest() {
        // given
        Long userId = 1L;
        lectureEnrollmentJpaRepository.saveAll(List.of(
                new LectureEnrollmentEntity(1L, 1L, userId),
                new LectureEnrollmentEntity(2L, 2L, 6L)
                )
        );

        // when
        List<LectureEnrollment> lectureEnrollments = lectureQueryService.findLectureEnrollments(userId);

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
        List<LectureEntity> lectureEntities = lectureJpaRepository.saveAll(List.of(
                        new LectureEntity(null, "title1", "description1", 1L, LocalDateTime.now(), null),
                        new LectureEntity(null, "title2", "description2", 2L, LocalDateTime.now(), null)
                )
        );

        // when
        List<Lecture> result = lectureQueryService.findLecturesByIds(List.of(lectureEntities.get(0).getId(), lectureEntities.get(1).getId()));

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