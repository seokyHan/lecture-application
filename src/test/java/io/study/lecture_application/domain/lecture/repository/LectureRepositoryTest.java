package io.study.lecture_application.domain.lecture.repository;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEnrollmentEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureEnrollmentJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureScheduleJpaRepository;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private LectureJpaRepository lectureJpaRepository;
    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;

    @AfterEach
    void setUp() {
        lectureJpaRepository.deleteAllInBatch();
        lectureEnrollmentJpaRepository.deleteAllInBatch();
        lectureScheduleJpaRepository.deleteAllInBatch();
    }

    @DisplayName("존재하지 않는 lectureId를 통해 조회할 경우 예외가 발생한다")
    @Test
    void shouldThrowExceptionNotExistLectureId() {

        //when & then
        assertThatThrownBy(() -> lectureRepository.getLectureById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(LECTURE_NOT_FOUND.getMessage());
    }

    @DisplayName("id를 통해 강의 정보를 조회한다.")
    @Test
    void getLectureByIdTest() {
         // given
         String title = "title";
         String description = "description";
         Long lecturerId = 2L;
         LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, title, description, lecturerId, null, null));

        // when
        Lecture lecture = lectureRepository.getLectureById(lectureEntity.getId());

        // then
        assertEquals(lecture.id(), lectureEntity.getId());
        assertEquals(lecture.title(), title);
        assertEquals(lecture.description(), description);
        assertEquals(lecture.lecturerId(), lecturerId);
        assertNotNull(lecture.createdAt());
        assertThat(lecture.updatedAt()).isNull();
    }

    @DisplayName("존재하지 않는 lectureScheduleId를 통해 조회할 경우 예외가 발생한다")
    @Test
    void shouldThrowExceptionNotExistLectureScheduleId() {

        //when & then
        assertThatThrownBy(() -> lectureRepository.getLectureScheduleById(1L, false))
                .isInstanceOf(CustomException.class)
                .hasMessage(LECTURE_SCHEDULE_NOT_FOUND.getMessage());
    }

    @DisplayName("lectureScheduleId를 통해 특강을 조회한다.")
    @Test
    void getLectureScheduleByIdTest() {
        // given
        int capacity = 30;
        int enrolledCount = 0;
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, lectureEntity.getId(), capacity,
                enrolledCount, LocalDateTime.now(), LocalDateTime.now(), null, null));

        // when
        LectureSchedule lectureSchedule = lectureRepository.getLectureScheduleById(lectureScheduleEntity.getId(), false);

        // then
        assertEquals(lectureSchedule.id(), lectureScheduleEntity.getId());
        assertEquals(lectureSchedule.lectureId(), lectureEntity.getId());
        assertEquals(lectureSchedule.capacity(), capacity);
        assertEquals(lectureSchedule.enrolledCount(), enrolledCount);
        assertNotNull(lectureSchedule.startAt());
        assertNotNull(lectureSchedule.endAt());
    }

    @DisplayName("입력받은 날짜를 통해 특강신청이 가능한 날짜를 조회한다.")
    @Test
    void findAvailableLectureSchedulesByDateTimeTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 1L, 30, 0, now.minusDays(1), now.minusDays(1), null, null));
        lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 2L, 30, 0, now, now, null, null));
        lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 3L, 10, 5, now.plusDays(1), now.plusDays(1), null, null));

        // when
       List<LectureSchedule> lectureSchedules = lectureRepository.findAvailableLectureSchedulesByDateTime(now.toLocalDate());

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


    @DisplayName("존재하지 않는 lectureScheduleId를 통해 조회할 경우 예외가 발생한다")
    @Test
    void shouldThrowExceptionNotExistLectureEnrollmentId() {

        //when & then
        assertThatThrownBy(() -> lectureRepository.getLectureEnrollmentById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(LECTURE_ENROLLMENT_NOT_FOUND.getMessage());
    }

    @DisplayName("lectureEnrollmentId를 통해 특강신청이력을  조회한다.")
    @Test
    void getLectureEnrollmentByIdTest() {
        // given
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        Long userId = 1L;
        LectureEnrollmentEntity lectureEnrollmentEntity = lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(lectureId, lectureScheduleId, userId));

        // when
        LectureEnrollment lectureEnrollment = lectureRepository.getLectureEnrollmentById(lectureEnrollmentEntity.getId());

        // then
        assertEquals(lectureEnrollment.id(), lectureEnrollmentEntity.getId());
        assertEquals(lectureEnrollment.lectureId(), lectureId);
        assertEquals(lectureEnrollment.lectureScheduleId(), lectureScheduleId);
        assertEquals(lectureEnrollment.userId(), userId);
        assertNotNull(lectureEnrollment.createdAt());
    }

    @DisplayName("userId를 통해 특강신청 이력을  조회한다.")
    @Test
    void findLectureEnrollmentsByUserIdTest() {
        // given
        Long userId = 1L;
        lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(1L, 1L, userId));
        lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(2L, 2L, 99L));

        // when
        List<LectureEnrollment> lectureEnrollments = lectureRepository.findLectureEnrollmentsByUserId(userId);

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

    @DisplayName("lectureIds를 통해 특강을 조회한다.")
    @Test
    void findLecturesByIdsTest() {
        // given
        List<LectureEntity> lectureEntities = List.of(lectureJpaRepository.save(new LectureEntity(null, "title1", "description1", 1L, null, null)));
        List<Long> lectureIds = lectureEntities.stream()
                .map(LectureEntity::getId)
                .toList();
        lectureJpaRepository.save(new LectureEntity(null, "title2", "description2", 3L, null, null));

        // when
        List<Lecture> lectures = lectureRepository.findLecturesByIds(lectureIds);

        // then
        assertThat(lectures).hasSize(1)
                .extracting("id", "title", "description", "createdAt")
                .containsExactlyInAnyOrder(
                        tuple(lectures.get(0).id(),
                                lectures.get(0).title(),
                                lectures.get(0).description(),
                                lectures.get(0).createdAt()
                        )
                );
    }

    @DisplayName("lectureId, lectureScheduleId, userId를 입력받아서 특강신청이력 테이블에 값을 저장한다.")
    @Test
    void createLectureEnrollmentTest() {
        // given
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        Long userId = 1L;

        // when
        Long result = lectureRepository.createLectureEnrollment(lectureId, lectureScheduleId, userId);
        LectureEnrollmentEntity lectureEnrollmentEntity = lectureEnrollmentJpaRepository.findById(result).get();

        // then
        assertNotNull(result);
        assertEquals(lectureEnrollmentEntity.getLectureId(), lectureId);
        assertEquals(lectureEnrollmentEntity.getLectureScheduleId(), lectureScheduleId);
        assertEquals(lectureEnrollmentEntity.getUserId(), userId);
        assertNotNull(lectureEnrollmentEntity.getCreatedAt());
    }

    @DisplayName("현재 등록된 특강자 수를 증가시킨다.")
    @Test
    void increaseEnrollmentCountByLectureScheduleIdTest() {
        // given
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(new LectureScheduleEntity(null, 1L, 30, 0, LocalDateTime.now(), LocalDateTime.now(), null, null));

        // when
        lectureRepository.increaseEnrollmentCountByLectureScheduleId(lectureScheduleEntity.getId());
        LectureScheduleEntity updatedLectureScheduleEntity = lectureScheduleJpaRepository.findById(lectureScheduleEntity.getId()).get();

        // then
        assertEquals(updatedLectureScheduleEntity.getEnrolledCount(), lectureScheduleEntity.getEnrolledCount() + 1);
    }


}