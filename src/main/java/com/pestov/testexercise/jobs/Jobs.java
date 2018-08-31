package com.pestov.testexercise.jobs;

import com.pestov.testexercise.services.IUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Jobs {

	private final IUserService userService;

	public Jobs(IUserService userService) {
		this.userService = userService;
	}

	@Scheduled(cron = "0 0 1 * * *")
	public void deleteExpiredBooksharings() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		userService.deleteExpiredBooksharings(yesterday);
	}
}
