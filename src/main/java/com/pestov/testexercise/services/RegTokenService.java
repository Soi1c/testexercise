package com.pestov.testexercise.services;

import com.pestov.testexercise.models.RegToken;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.repositories.RegTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegTokenService implements IRegTokenService {

	@Autowired
	private RegTokenRepository regTokenRepository;

	public String saveNewRegToken(User user) {
		RegToken regToken = new RegToken(user);
		String result = UUID.randomUUID().toString();
		regToken.setRegToken(result);
		regToken.setCreationTime(LocalDateTime.now());
		regTokenRepository.save(regToken);
		return result;
	}
}
