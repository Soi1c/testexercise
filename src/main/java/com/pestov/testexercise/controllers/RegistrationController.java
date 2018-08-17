package com.pestov.testexercise.controllers;

import com.pestov.testexercise.services.IRegTokenService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "signup")
public class RegistrationController {

    private final IUserService userService;
	private final IRegTokenService regTokenService;

	public RegistrationController(IUserService userService, IRegTokenService regTokenService) {
		this.userService = userService;
		this.regTokenService = regTokenService;
	}

	@RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public void submit(@RequestBody String request) {
        userService.registerNewUser(request);
    }

	@RequestMapping(value = "confirmEmail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity approveUser(@RequestParam String token) {
		if (regTokenService.approveUserAndDeleteToken(token)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}
