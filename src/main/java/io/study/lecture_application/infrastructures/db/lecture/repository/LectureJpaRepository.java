package io.study.lecture_application.infrastructures.db.lecture.repository;


import io.study.lecture_application.infrastructures.db.lecture.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

}
