package io.study.lecture_application.interfaces.api.dto;

import io.study.lecture_application.domain.lecture.dto.LectureResult;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;

import java.time.LocalDate;
import java.util.List;

public class LectureHttp {

    public class EnrollLecture {

        public record Request(
                Long lectureId,
                Long lectureScheduleId,
                Long userId
        ) { }

        public record Response(LectureEnrollment lectureEnrollment) {}

    }

    public class EnrolledLectures {

        public record Request(Long userId) { }

        public record Response(List<LectureResult.ContainsLecturer> lectureEnrollments) { }

    }

    public class AvailableLectureSchedules {

        public record Request(LocalDate date) { }

        public record Response(List<LectureSchedule> lectureSchedules) { }

    }

}
