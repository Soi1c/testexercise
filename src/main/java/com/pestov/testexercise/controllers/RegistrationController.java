package com.pestov.testexercise.controllers;

import com.pestov.testexercise.captcha.CaptchaService;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.services.IUserService;
import com.pestov.testexercise.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/signup")
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @Autowired
	private CaptchaService captchaService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public void submit(@RequestBody String request, HttpServletRequest servletRequest) throws Exception {
		UserDto userDto = new UserDto(request);
    	captchaService.processResponse(userDto.getgCaptchaResponse());
        userService.registerNewUser(request);
    }
}
