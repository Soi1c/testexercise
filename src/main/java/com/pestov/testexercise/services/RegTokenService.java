package com.pestov.testexercise.services;

import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.models.RegToken;
import com.pestov.testexercise.repositories.RegTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegTokenService implements IRegTokenService {

	@Autowired
	private RegTokenRepository regTokenRepository;

	public String saveNewRegToken(CustomUser customUser) {
		RegToken regToken = new RegToken(customUser);
		String result = UUID.randomUUID().toString();
		regToken.setRegToken(result);
		regToken.setCreationTime(LocalDateTime.now());
		regTokenRepository.save(regToken);
		return result;
	}

	public boolean approveUserAndDeleteToken(String token) {
		RegToken regToken = regTokenRepository.findByRegToken(token);
		if (isTokenStillActive(regToken)) {
			regToken.getUserId().setActive(true);
			regTokenRepository.delete(regToken);
			return true;
		} else {
			regTokenRepository.delete(regToken);
			return false;
		}
	}

	private boolean isTokenStillActive(RegToken regToken) {
		return LocalDateTime.now().isBefore(regToken.getCreationTime().plusDays(1));
	}
}
