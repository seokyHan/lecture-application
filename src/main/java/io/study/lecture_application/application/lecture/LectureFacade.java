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
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.study.lecture_application.common.code.LectureErrorCode.ENROLLMENT_EXCEED_CAPACITY;

@Component
@RequiredArgsConstructor
public class LectureFacade {
    private final LectureService lectureService;
    private final LectureQueryService lectureQueryService;
    private final UserService userService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LectureEnrollment enrollLecture(Long lectureId, Long lectureScheduleId, Long userId) {
        val user = userService.getUser(new UserInfo.GetUserById(userId));
        val lecture = lectureQueryService.getLectureById(lectureId);
        val lectureSchedule = lectureQueryService.getLectureScheduleById(lectureScheduleId, true);

        // 정원 초과할 경우 예외 발생
        if (lectureSchedule.enrolledCount() + 1 > lectureSchedule.capacity()) {
            throw new CustomException(ENROLLMENT_EXCEED_CAPACITY);
        }

        val enrollmentId = lectureService.createLectureEnrollment(new LectureInfo.CreateEnrollmentLecture(lecture.id(), lectureSchedule.id(), user.id()));
        lectureService.increaseEnrollmentCountByLectureScheduleId(lectureSchedule.id());

        return lectureQueryService.getLectureEnrollmentById(enrollmentId);
    }

    public List<LectureResult.ContainsLecturer> getEnrolledLectures(Long userId) {
        val user = userService.getUser(new UserInfo.GetUserById(userId));
        val enrolledLectures = lectureQueryService.findLectureEnrollments(user.id());
        val lectureIds = enrolledLectures.stream()
                .map(LectureEnrollment::lectureId)
                .toList();
        val lectures = lectureQueryService.findLecturesByIds(lectureIds);

        val lecturerIds = lectures.stream()
                .map(Lecture::lecturerId)
                .toList();
        val lecturers = userService.getUsers(new UserInfo.GetUserByIds(lecturerIds));

        val lecturerMap = lecturers.stream()
                .collect(Collectors.toMap(Users::id, Function.identity()));

        return lectures.stream()
                .map(lecture -> {
                    Users lecturer = lecturerMap.get(lecture.lecturerId());
                    return LectureResult.ContainsLecturer.of(lecture, lecturer);
                })
                .toList();
    }

    public List<LectureSchedule> getAvailableLectureSchedules(LocalDate date) {
        return lectureQueryService.findAvailableLectureSchedules(date);
    }

}
