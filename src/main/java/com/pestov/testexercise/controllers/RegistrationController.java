package com.pestov.testexercise.controllers;

import com.pestov.testexercise.Dto.UserDto;
import com.pestov.testexercise.models.User;
import com.pestov.testexercise.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/signup")
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public String submit(@ModelAttribute("user") UserDto userDto, BindingResult result) {
        userService.registerNewUser(userDto);
        return "redirect:index.html";
    }

}
