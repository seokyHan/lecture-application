package io.study.lecture_application.application.lecture;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.domain.lecture.dto.LectureInfo;
import io.study.lecture_application.domain.lecture.dto.LectureResult;
import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.domain.lecture.service.LectureQueryService;
import io.study.lecture_application.domain.lecture.service.LectureService;
import io.study.lecture_application.domain.users.dto.UserInfo;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.domain.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static io.study.lecture_application.common.code.LectureErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureFacadeUnitTest {

    @InjectMocks
    private LectureFacade lectureFacade;
    @Mock
    private LectureQueryService lectureQueryService;
    @Mock
    private LectureService lectureService;
    @Mock
    private UserService userService;

    @DisplayName("수강생이 모두 차있는 특강 신청시 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenExceedCapacity() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        Users user = new Users(userId, "이름", null, null);
        Lecture lecture = new Lecture(lectureId, "제목", "설명", 2L, null, null);
        LectureSchedule lectureSchedule = new LectureSchedule(lectureScheduleId, lectureId, 30,
                30, null, null, null, null);

        when(userService.getUser(new UserInfo.GetUserById(userId))).thenReturn(user);
        when(lectureQueryService.getLectureById(lectureId)).thenReturn(lecture);
        when(lectureQueryService.getLectureScheduleById(lectureScheduleId, false)).thenReturn(lectureSchedule);

        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ENROLLMENT_EXCEED_CAPACITY.getMessage());
    }



    @DisplayName("수강생이 이미 특강신청이 되어있는 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenDuplicateEnrollment() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        Users user = new Users(userId, "이름", null, null);
        Lecture lecture = new Lecture(lectureId, "제목", "설명", 2L, null, null);
        LectureSchedule lectureSchedule = new LectureSchedule(lectureScheduleId, lectureId, 30,
                0, null, null, null, null);

        when(userService.getUser(new UserInfo.GetUserById(userId))).thenReturn(user);
        when(lectureQueryService.getLectureById(lectureId)).thenReturn(lecture);
        when(lectureQueryService.getLectureScheduleById(lectureScheduleId, false)).thenReturn(lectureSchedule);
        when(lectureService.createLectureEnrollment(new LectureInfo.CreateEnrollmentLecture(lectureId, lectureScheduleId, userId))).thenThrow(new CustomException(DUPLICATE_ENROLLMENT));

        // when & then
        assertThatThrownBy(() -> lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(DUPLICATE_ENROLLMENT.getMessage());
    }


    @DisplayName("특강 신청을 한다.")
    @Test
    void enrollLectureTest() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        Long lectureEnrollmentId = 1L;
        Users user = new Users(userId, "이름", null, null);
        Lecture lecture = new Lecture(lectureId, "제목", "설명", 2L, null, null);
        LectureSchedule lectureSchedule = new LectureSchedule(lectureScheduleId, lectureId, 30,
                0,null, null, null, null);
        LectureEnrollment lectureEnrollment = new LectureEnrollment(lectureEnrollmentId, lectureId,
                lectureScheduleId, userId, null, null);

        when(userService.getUser(new UserInfo.GetUserById(userId))).thenReturn(user);
        when(lectureQueryService.getLectureById(lectureId)).thenReturn(lecture);
        when(lectureQueryService.getLectureScheduleById(lectureScheduleId, false)).thenReturn(lectureSchedule);
        when(lectureService.createLectureEnrollment(new LectureInfo.CreateEnrollmentLecture(lectureId, lectureScheduleId, userId)))
                .thenReturn(lectureEnrollmentId);
        when(lectureQueryService.getLectureEnrollmentById(lectureEnrollmentId)).thenReturn(lectureEnrollment);

        // when
        LectureEnrollment result = lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId);

        // then
        assertEquals(result.id(), lectureEnrollmentId);
        assertEquals(result.lectureId(), lectureId);
        assertEquals(result.lectureScheduleId(), lectureScheduleId);
        assertEquals(result.userId(), userId);
        assertEquals(result.createdAt(), lectureEnrollment.createdAt());
        assertEquals(result.updatedAt(), lectureEnrollment.updatedAt());
    }

    @DisplayName("특강신청 이력을 조회한다.")
    @Test
    void getEnrolledLecturesTest() {
        // given
        Long userId = 1L;
        Users user = new Users(userId, "name", null, null);
        List<LectureEnrollment> lectureEnrollments = List.of(
                new LectureEnrollment(1L, 2L, 1L, userId, null, null),
                new LectureEnrollment(2L, 3L, 2L, userId, null, null)
        );
        List<Lecture> lectures = List.of(
                new Lecture(1L, "제목1", "설명1", 2L, null, null),
                new Lecture(2L, "제목2", "설명2", 3L, null, null)
        );
       List<Users> lecturers = List.of(
                new Users(2L, "강사1", null, null),
                new Users(3L, "강사2", null, null)
        );
        List<Long> lectureIds = lecturers.stream().map(Users::id).toList();
        List<Long> lecturerIds = lecturers.stream().map(Users::id).toList();

        when(userService.getUser(new UserInfo.GetUserById(userId))).thenReturn(user);
        when(lectureQueryService.findLectureEnrollments(userId)).thenReturn(lectureEnrollments);
        when(lectureQueryService.findLecturesByIds(lectureIds)).thenReturn(lectures);
        when(userService.getUsers(new UserInfo.GetUserByIds(lecturerIds)))
                .thenReturn(lecturers);
        // when
        List<LectureResult.ContainsLecturer> enrolledLectures = lectureFacade.getEnrolledLectures(userId);

        // then
        assertThat(enrolledLectures).hasSize(2);
        for (int i = 0; i < enrolledLectures.size(); i++) {
            LectureResult.ContainsLecturer containsLecturer = enrolledLectures.get(i);
            Lecture lecture = lectures.get(i);
            Users lecturer = lecturers.get(i);

            assertEquals(containsLecturer.id(), lecture.id());
            assertEquals(containsLecturer.title(), lecture.title());
            assertEquals(containsLecturer.description(), lecture.description());
            assertEquals(containsLecturer.createdAt(), lecture.createdAt());
            assertEquals(containsLecturer.updatedAt(), lecture.updatedAt());
            assertEquals(containsLecturer.lecturer().id(), lecturer.id());
            assertEquals(containsLecturer.lecturer().name(), lecturer.name());
            assertEquals(containsLecturer.lecturer().createdAt(), lecturer.createdAt());
            assertEquals(containsLecturer.lecturer().updatedAt(), lecturer.updatedAt());
        }
    }



    @Test
    @DisplayName("특강신청이 가능한 특강들을 조회한다.")
    void getAvailableLectureSchedulesTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<LectureSchedule> lectureSchedules = List.of(
                new LectureSchedule(1L, 1L, 30, 0, now, now, now, null),
                new LectureSchedule(2L, 2L, 10, 5, now, now, now, null)
        );
        when(lectureQueryService.findAvailableLectureSchedules(now.toLocalDate())).thenReturn(lectureSchedules);

        // when
        List<LectureSchedule> availableLectureSchedules = lectureFacade.getAvailableLectureSchedules(now.toLocalDate());

        // then
        assertThat(availableLectureSchedules).hasSize(2)
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
}