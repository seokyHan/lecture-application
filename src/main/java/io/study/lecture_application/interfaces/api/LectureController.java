package io.study.lecture_application.interfaces.api;

import io.study.lecture_application.application.lecture.LectureFacade;
import io.study.lecture_application.interfaces.api.dto.LectureHttp;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureFacade lectureFacade;

    @PostMapping("/enrollments")
    public ResponseEntity<LectureHttp.EnrollLecture.Response> enrollLecture(@RequestBody LectureHttp.EnrollLecture.Request request) {
        val lectureEnrollment = lectureFacade.enrollLecture(request.lectureId(), request.lectureScheduleId(), request.userId());

        return ResponseEntity.ok(new LectureHttp.EnrollLecture.Response(lectureEnrollment));
    }

    @GetMapping("/enrolled")
    public ResponseEntity<LectureHttp.EnrolledLectures.Response> getEnrolledLectures(@RequestBody LectureHttp.EnrolledLectures.Request request) {
        val lectureEnrollments = lectureFacade.getEnrolledLectures(request.userId());

        return ResponseEntity.ok(new LectureHttp.EnrolledLectures.Response(lectureEnrollments));
    }

    @GetMapping("/schedules/available")
    public ResponseEntity<LectureHttp.AvailableLectureSchedules.Response> getAvailableLectureSchedules(@RequestBody LectureHttp.AvailableLectureSchedules.Request request) {
        val lectureSchedules = lectureFacade.getAvailableLectureSchedules(request.date());

        return ResponseEntity.ok(new LectureHttp.AvailableLectureSchedules.Response(lectureSchedules));
    }

}
