package com.pestov.testexercise.captcha;

import com.pestov.testexercise.captcha.CaptchaSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

	@Autowired
	private CaptchaSettings captchaSettings;

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private HttpServletRequest servletRequest;

	private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	public void processResponse(String response) throws Exception {
		if(!responseSanityCheck(response)) {
			throw new Exception("Response contains invalid characters");
		}

		URI verifyUri = URI.create(String.format(
				"https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
				getReCaptchaSecret(), response, getClientIP()));

		GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

		if(!googleResponse.isSuccess()) {
			throw new Exception("reCaptcha was not successfully validated");
		}
	}

	private String getReCaptchaSecret() {
		return captchaSettings.getSecret();
	}

	private boolean responseSanityCheck(String response) {
		return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
	}

	private String getClientIP() {
		final String xfHeader = servletRequest.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return servletRequest.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}