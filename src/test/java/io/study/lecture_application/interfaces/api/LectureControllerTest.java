package io.study.lecture_application.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.study.lecture_application.application.lecture.LectureFacade;
import io.study.lecture_application.domain.lecture.dto.LectureResult;
import io.study.lecture_application.domain.lecture.repository.model.LectureEnrollment;
import io.study.lecture_application.domain.lecture.repository.model.LectureSchedule;
import io.study.lecture_application.domain.users.model.Users;
import io.study.lecture_application.interfaces.api.dto.LectureHttp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LectureController.class)
class LectureControllerTest {

    @MockBean
    private LectureFacade lectureFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("강의 등록 테스트")
    @Test
    void enrollLectureTest() throws Exception {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        Long lectureScheduleId = 1L;
        LectureHttp.EnrollLecture.Request request = new LectureHttp.EnrollLecture.Request(lectureId, lectureScheduleId, userId);
        LectureEnrollment lectureEnrollment = new LectureEnrollment(1L, lectureId, lectureScheduleId, userId, null, null);
        when(lectureFacade.enrollLecture(lectureId, lectureScheduleId, userId)).thenReturn(lectureEnrollment);

        // when & then
        mockMvc.perform(
                post("/api/v1/lectures/enrollments")
                        .content(mapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureEnrollment.userId").value(userId))
                .andExpect(jsonPath("$.lectureEnrollment.lectureId").value(lectureId))
                .andExpect(jsonPath("$.lectureEnrollment.lectureScheduleId").value(lectureScheduleId));
    }

    @DisplayName("등록된 강의 조회 테스트")
    @Test
    void getEnrolledLecturesTest() throws Exception {
        // given
        long userId = 1L;
        LectureHttp.EnrolledLectures.Request request = new LectureHttp.EnrolledLectures.Request(1L);
        Users users = new Users(userId, "name", LocalDateTime.now(), null);
        LectureResult.ContainsLecturer containsLecturer = new LectureResult.ContainsLecturer(1L, "title", "description", users, LocalDateTime.now(), null);
        when(lectureFacade.getEnrolledLectures(userId)).thenReturn(List.of(containsLecturer));

        // when & then
        mockMvc.perform(
                get("/api/v1/lectures/enrolled")
                        .content(mapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureEnrollments", hasSize(1)))
                .andExpect(jsonPath("$.lectureEnrollments[0].title").value("title"))
                .andExpect(jsonPath("$.lectureEnrollments[0].description").value("description"))
                .andExpect(jsonPath("$.lectureEnrollments[0].lecturer.id").value(userId))
                .andExpect(jsonPath("$.lectureEnrollments[0].lecturer.name").value("name"));
    }

    @DisplayName("사용 가능한 강의 일정 조회 테스트")
    @Test
    void getAvailableLectureSchedulesTest() throws Exception {
        // Given
        LocalDate now = LocalDate.now();
        LectureHttp.AvailableLectureSchedules.Request request = new LectureHttp.AvailableLectureSchedules.Request(LocalDate.now());
        LectureSchedule lectureSchedule = new LectureSchedule(1L, 2L, 30, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now(), null);
        when(lectureFacade.getAvailableLectureSchedules(now)).thenReturn(List.of(lectureSchedule));

        // When & Then
        mockMvc.perform(
                get("/api/v1/lectures/schedules/available")
                        .content(mapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureSchedules", hasSize(1)))
                .andExpect(jsonPath("$.lectureSchedules[0].id").value(1L))
                .andExpect(jsonPath("$.lectureSchedules[0].lectureId").value(2L))
                .andExpect(jsonPath("$.lectureSchedules[0].capacity").value(30))
                .andExpect(jsonPath("$.lectureSchedules[0].enrolledCount").value(5));
    }


}