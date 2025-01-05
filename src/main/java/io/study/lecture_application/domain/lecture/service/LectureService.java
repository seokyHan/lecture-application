package io.study.lecture_application.domain.lecture.service;

import io.study.lecture_application.common.exception.CustomException;
import io.study.lecture_application.common.validator.LectureValidator;
import io.study.lecture_application.domain.lecture.dto.LectureInfo;
import io.study.lecture_application.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static io.study.lecture_application.common.code.LectureErrorCode.DUPLICATE_ENROLLMENT;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    public Long createLectureEnrollment(LectureInfo.CreateEnrollmentLecture lectureInfo) {
        LectureValidator.createLectureEnrollmentValidate(lectureInfo.lectureId(), lectureInfo.lectureScheduleId(), lectureInfo.userId());
        try {
            return lectureRepository.createLectureEnrollment(lectureInfo.lectureId(), lectureInfo.lectureScheduleId(), lectureInfo.userId());
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(DUPLICATE_ENROLLMENT);
        }
    }

    public void increaseEnrollmentCountByLectureScheduleId(Long lectureScheduleId) {
        lectureRepository.increaseEnrollmentCountByLectureScheduleId(lectureScheduleId);
    }
}
