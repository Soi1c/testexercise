package com.pestov.testexercise.controllers;

import com.pestov.testexercise.models.User;
import com.pestov.testexercise.services.IRegTokenService;
import com.pestov.testexercise.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/signup")
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public void submit(@RequestBody String request) {
        userService.registerNewUser(request);
    }
}
