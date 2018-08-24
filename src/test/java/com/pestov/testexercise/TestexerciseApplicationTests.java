package com.pestov.testexercise;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-test.properties")
public class TestexerciseApplicationTests {
	protected String authTokenForUserTest1 = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzNTYxNjI0NX0.nABPIVbaf6ivyWc4vFoVWTiXr-PmaumvjcpshlqzCuY";
	protected String authTokenForUserTest2 = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MkB0ZXN0LmNvbSIsImV4cCI6MTUzNTgwMzUxM30.Qlho8eCDRjvdyhcqzWr1KcA9EFnMB0qyRNzIn-pR9UE";
}
