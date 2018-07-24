package com.pestov.testexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAutoConfiguration
public class TestexerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestexerciseApplication.class, args);
	}
}