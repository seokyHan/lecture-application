package io.study.lecture_application.application.lecture;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.lecture.dto.LectureResult;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEnrollmentEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEntity;
import io.study.lecture_application.infrastructures.db.lecture.entity.LectureScheduleEntity;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureEnrollmentJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureJpaRepository;
import io.study.lecture_application.infrastructures.db.lecture.repository.LectureScheduleJpaRepository;
import io.study.lecture_application.infrastructures.db.users.entity.UsersEntity;
import io.study.lecture_application.infrastructures.db.users.repository.UsersJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static io.study.lecture_application.common.code.LectureErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureFacadeIntegrationTest {

    @Autowired
    private LectureFacade lectureFacade;
    @Autowired
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private LectureJpaRepository lectureJpaRepository;
    @Autowired
    private LectureScheduleJpaRepository lectureScheduleJpaRepository;
    @Autowired
    private LectureEnrollmentJpaRepository lectureEnrollmentJpaRepository;


    @BeforeEach
    void setUp() {
        usersJpaRepository.deleteAllInBatch();
        lectureJpaRepository.deleteAllInBatch();
        lectureScheduleJpaRepository.deleteAllInBatch();
        lectureEnrollmentJpaRepository.deleteAllInBatch();
    }

    @DisplayName("유저가 존재하지 않을경우 특강신청에 실패한다.")
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> lectureFacade.enrollLecture(1L, 1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }


    @DisplayName("특강이 존재하지 않을 경우 특강신청에 실패한다.")
    @Test
    void shouldThrowExceptionWhenLectureNotFound() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));


        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(1L, 1L, userEntity.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(LECTURE_NOT_FOUND.getMessage());
    }

    @DisplayName("특강의 스케줄이 존재하지 않은 경우 특강신청에 실패한다.")
    @Test
    void shouldThrowExceptionWhenLectureScheduleNotFound() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));


        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(lectureEntity.getId(), 1L, userEntity.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(LECTURE_SCHEDULE_NOT_FOUND.getMessage());
    }

    @DisplayName("수강 신청 인원 초과시 특강 신청에 실패한다.")
    @Test
    void shouldThrowExceptionWhenExceedCapacity() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(
                new LectureScheduleEntity(null, lectureEntity.getId(), 30, 30, LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1), null, null));

        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(lectureEntity.getId(), lectureScheduleEntity.getId(), userEntity.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ENROLLMENT_EXCEED_CAPACITY.getMessage());
    }

    @Test
    @DisplayName("이미 수강중인 특강일 경우 특강 신청에 실패한다.")
    void shouldThrowExceptionWhenAlreadyEnrolled() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(
          new LectureScheduleEntity(null, lectureEntity.getId(), 30, 0, LocalDateTime.now(),
                  LocalDateTime.now().plusHours(1), null, null)
        );
        lectureEnrollmentJpaRepository.save(new LectureEnrollmentEntity(lectureEntity.getId(), lectureScheduleEntity.getId(), userEntity.getId()));

        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(lectureEntity.getId(), lectureScheduleEntity.getId(), userEntity.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_ENROLLMENT.getMessage());
    }

    @DisplayName("특강 신청을 성공한다.")
    @Test
    void successfullyEnrollLectureTest() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(
                new LectureScheduleEntity(null, lectureEntity.getId(), 30, 0, LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1), null, null)
        );

        // when
        LectureEnrollment lectureEnrollment = lectureFacade.enrollLecture(lectureEntity.getId(), lectureScheduleEntity.getId(), userEntity.getId());
        LectureScheduleEntity updatedLectureScheduleEntity = lectureScheduleJpaRepository.findById(lectureScheduleEntity.getId()).get();

        // then
        assertEquals(lectureEnrollment.lectureId(), lectureEntity.getId());
        assertEquals(lectureEnrollment.lectureScheduleId(), lectureScheduleEntity.getId());
        assertEquals(lectureEnrollment.userId(), userEntity.getId());
        assertNotNull(lectureEnrollment.createdAt());
        assertThat(lectureEnrollment.updatedAt()).isNull();

        assertEquals(updatedLectureScheduleEntity.getEnrolledCount(),lectureScheduleEntity.getEnrolledCount() + 1);
    }

    @Test
    @DisplayName("특강신청 이력을 조회한다.")
    void getEnrolledLecturesTest() {
        // given
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        List<UsersEntity> lecturers = usersJpaRepository.saveAll(List.of(
                new UsersEntity(null, "강사1", null, null),
                new UsersEntity(null, "강사2", null, null)
        ));
        List<LectureEntity> lectures = lectureJpaRepository.saveAll(List.of(
                new LectureEntity(null, "강의1", "설명1", lecturers.get(0).getId(), null, null),
                new LectureEntity(null, "강의2", "설명2", lecturers.get(1).getId(), null, null)
        ));
        List<Long> lectureIds = lectures.stream()
                .map(LectureEntity::getId)
                .toList();
        lectureEnrollmentJpaRepository.saveAll(
                List.of(
                        new LectureEnrollmentEntity(lectureIds.get(0), 1L, userEntity.getId()),
                        new LectureEnrollmentEntity(lectureIds.get(1), 2L, userEntity.getId())
                ));

        // when
        List<LectureResult.ContainsLecturer> enrolledLectures = lectureFacade.getEnrolledLectures(userEntity.getId());

        // then
        assertThat(enrolledLectures).hasSize(2);
        for (int i = 0; i < enrolledLectures.size(); i++) {
            LectureResult.ContainsLecturer containsLecturer = enrolledLectures.get(i);
            LectureEntity lectureEntity = lectures.get(i);
            UsersEntity lecturer = lecturers.get(i);

            assertEquals(containsLecturer.id(), lectureEntity.getId());
            assertEquals(containsLecturer.title(), lectureEntity.getTitle());
            assertEquals(containsLecturer.description(), lectureEntity.getDescription());
            assertEquals(containsLecturer.createdAt(), lectureEntity.getCreatedAt());
            assertEquals(containsLecturer.updatedAt(), lectureEntity.getUpdatedAt());
            assertEquals(containsLecturer.lecturer().id(), lecturer.getId());
            assertEquals(containsLecturer.lecturer().name(), lecturer.getName());
            assertEquals(containsLecturer.lecturer().createdAt(), lecturer.getCreatedAt());
            assertEquals(containsLecturer.lecturer().updatedAt(), lecturer.getUpdatedAt());
        }
    }

    @Test
    @DisplayName("특강 신청이 가능한 특강 목록을 조회한다.")
    void getAvailableLectureSchedulesTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<LectureScheduleEntity> lectureScheduleEntities = lectureScheduleJpaRepository.saveAll(List.of(
                new LectureScheduleEntity(11L, 1L, 30, 0, now, now, now, null),
                new LectureScheduleEntity(21L, 2L, 10, 5, now, now, now, null)
        ));

        // when
        List<LectureSchedule> availableLectureSchedules = lectureFacade.getAvailableLectureSchedules(now.toLocalDate());

        // then
        assertThat(availableLectureSchedules).hasSize(2)
                .extracting("id", "lectureId", "capacity", "enrolledCount", "startAt", "endAt")
                .containsExactlyInAnyOrder(
                        tuple(lectureScheduleEntities.get(0).getId(),
                                lectureScheduleEntities.get(0).getLectureId(),
                                lectureScheduleEntities.get(0).getCapacity(),
                                lectureScheduleEntities.get(0).getEnrolledCount(),
                                lectureScheduleEntities.get(0).getStartAt(),
                                lectureScheduleEntities.get(0).getEndAt()
                        ),
                        tuple(lectureScheduleEntities.get(1).getId(),
                                lectureScheduleEntities.get(1).getLectureId(),
                                lectureScheduleEntities.get(1).getCapacity(),
                                lectureScheduleEntities.get(1).getEnrolledCount(),
                                lectureScheduleEntities.get(1).getStartAt(),
                                lectureScheduleEntities.get(1).getEndAt()
                        )
                );
    }


    @Test
    @DisplayName("선착순 30명 특강 신청 성공 - 만일 30명이 넘어갈 경우 예외 발생")
    void successfullyEnrollLectureWithConcurrencyTest() {
        // given
        int threadCount = 30;
        List<UsersEntity> userEntities = usersJpaRepository.saveAll(
                IntStream.range(0, threadCount)
                        .mapToObj(i -> new UsersEntity(null, "name" + i, null, null))
                        .toList()
        );
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 3L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(
                new LectureScheduleEntity(null, lectureEntity.getId(), 30, 0,
                        LocalDateTime.now(),LocalDateTime.now().plusHours(1), null, null
                )
        );
        Long lectureId = lectureEntity.getId();
        Long lectureScheduleId = lectureScheduleEntity.getId();

        // when
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    UsersEntity userEntity = userEntities.get(i);
                    Long userId = userEntity.getId();
                    lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId);
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        LectureScheduleEntity updatedLectureScheduleEntity = lectureScheduleJpaRepository.findById(lectureScheduleId).get();

        // then
        assertEquals(updatedLectureScheduleEntity.getEnrolledCount(), threadCount);
    }

    @Test
    @DisplayName("동시에 동일한 특강에 40명 신청시 30명만 성공한다.")
    void successfullyEnrollLectureWithConcurrencyWhenExceedCapacityTest() {
        // given
        int threadCount = 40;
        List<UsersEntity> userEntities = usersJpaRepository.saveAll(
                IntStream.range(0, threadCount)
                        .mapToObj(i -> new UsersEntity(null, "name" + i, null, null))
                        .toList()
        );
        LectureEntity lectureEntity = lectureJpaRepository.save(
                new LectureEntity(null, "title", "description", 2L, null, null));
        LectureScheduleEntity lectureScheduleEntity = lectureScheduleJpaRepository.save(
                new LectureScheduleEntity(null, lectureEntity.getId(), 30, 0, LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1), null, null));
        Long lectureId = lectureEntity.getId();
        Long lectureScheduleId = lectureScheduleEntity.getId();

        // when
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        final UsersEntity userEntity = userEntities.get(i);
                        final Long userId = userEntity.getId();
                        lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId);
                    } catch (CustomException e) {
                        assertThat(e.getMessage()).isEqualTo(ENROLLMENT_EXCEED_CAPACITY
                                .getMessage());
                    }
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        LectureScheduleEntity updatedLectureScheduleEntity = lectureScheduleJpaRepository.findById(lectureScheduleId).get();
        List<LectureEnrollmentEntity> lectureEnrollments = lectureEnrollmentJpaRepository.findAllByLectureScheduleId(lectureScheduleId);

        // then
        assertEquals(updatedLectureScheduleEntity.getEnrolledCount(),30);
        assertThat(lectureEnrollments).hasSize(30);
    }

    @Test
    @DisplayName("동일 유저 정보로 동시에 5번 신청했을 때 1번만 신청 성공한다.")
    void successfullyEnrollLectureWithConcurrencyWhenAlreadyEnrolledTest() {
        // given
        int threadCount = 5;
        UsersEntity userEntity = usersJpaRepository.save(new UsersEntity(null, "name", null, null));
        LectureEntity lectureEntity = lectureJpaRepository.save(new LectureEntity(null, "title", "description", 2L, null, null));
        List<LectureScheduleEntity> lectureScheduleEntities = lectureScheduleJpaRepository.saveAll(
                IntStream.range(0, threadCount)
                        .mapToObj(i -> new LectureScheduleEntity(null, lectureEntity.getId(), 30, 0,
                                LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null))
                        .toList()
        );
        Long userId = userEntity.getId();
        Long lectureId = lectureEntity.getId();

        // when
        final List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        final Long lectureScheduleId = lectureScheduleEntities.get(i).getId();
                        lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId);
                    } catch (CustomException e) {
                        // 중복 신청의 경우 예외 발생
                        assertEquals(e.getMessage(), DUPLICATE_ENROLLMENT.getMessage());
                    }
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        List<LectureEnrollmentEntity> lectureEnrollments = lectureEnrollmentJpaRepository.findAllByLectureId(lectureId);
        LectureEnrollmentEntity lectureEnrollment = lectureEnrollments.get(0);

        // then
        assertThat(lectureEnrollments).hasSize(1);
        assertEquals(lectureEnrollment.getLectureId(), lectureId);
        assertEquals(lectureEnrollment.getUserId(), userId);
    }


}