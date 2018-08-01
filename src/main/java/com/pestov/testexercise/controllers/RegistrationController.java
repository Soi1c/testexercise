package com.pestov.testexercise.controllers;

import com.pestov.testexercise.models.RegToken;
import com.pestov.testexercise.services.IRegTokenService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "signup")
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

	@RequestMapping(value = "confirmEmail", method = RequestMethod.GET)
	@ResponseBody
	public RedirectView approveUser(@RequestParam String token) {
		if (regTokenService.approveUserAndDeleteToken(token)) {
			return new RedirectView("/login.html");
		} else {
			return new RedirectView("/badToken.html");
		}
	}
}
