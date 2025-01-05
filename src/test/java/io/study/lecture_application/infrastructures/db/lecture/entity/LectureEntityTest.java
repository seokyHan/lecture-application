package io.study.lecture_application.infrastructures.db.lecture.entity;

import io.study.lecture_application.domain.lecture.repository.model.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LectureEntityTest {


    @DisplayName("LectureEntity 를 Lecture Model로 반환한다.")
    @Test
    void LectureEntityConvertToLectureModelTest() {
        //given
        LectureEntity lectureEntity = LectureEntity.builder()
                .id(1L)
                .lecturerId(2L)
                .title("JPA")
                .description("설명")
                .createdAt(LocalDateTime.now())
                .build();

        //when
        Lecture lecture = lectureEntity.toLectureDomain();

        //then
        assertEquals(lectureEntity.getId(), lecture.id());
        assertEquals(lectureEntity.getLecturerId(), lecture.lecturerId());
        assertEquals(lectureEntity.getTitle(), lecture.title());
        assertEquals(lectureEntity.getDescription(), lecture.description());
        assertEquals(lectureEntity.getCreatedAt(), lecture.createdAt());
    }

}