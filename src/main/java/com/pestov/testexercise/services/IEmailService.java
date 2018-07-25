package com.pestov.testexercise.services;

public interface IEmailService {

	void sendSimpleMessage(String to, String subject, String text);
}
