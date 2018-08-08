package com.pestov.testexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TestexerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestexerciseApplication.class, args);
	}
}
