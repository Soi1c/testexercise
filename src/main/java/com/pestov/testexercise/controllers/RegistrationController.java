package com.pestov.testexercise.controllers;

import com.pestov.testexercise.models.RegToken;
import com.pestov.testexercise.services.IRegTokenService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(name = "signup")
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @Autowired
	private IRegTokenService regTokenService;

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public void submit(@RequestBody String request) {
        userService.registerNewUser(request);
    }

	@RequestMapping(value = "{token}", method = RequestMethod.GET)
	@ResponseBody
	public String approveUser(@PathVariable String token) {
		regTokenService.approveUserAndDeleteToken(token);
		return "index";
	}
}
