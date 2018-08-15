package com.pestov.testexercise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

	private final JavaMailSender getJavaMailSender;

	public EmailService(JavaMailSender getJavaMailSender) {
		this.getJavaMailSender = getJavaMailSender;
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		getJavaMailSender.send(message);
	}
}
