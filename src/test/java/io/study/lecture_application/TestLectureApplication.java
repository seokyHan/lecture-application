package io.study.lecture_application;

import org.springframework.boot.SpringApplication;

public class TestLectureApplication {

	public static void main(String[] args) {
		SpringApplication.from(LectureApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
