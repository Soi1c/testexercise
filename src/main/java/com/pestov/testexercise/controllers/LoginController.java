//package com.pestov.testexercise.controllers;
//
//import com.pestov.testexercise.dto.UserDto;
//import com.pestov.testexercise.errors.ApiError;
//import com.pestov.testexercise.errors.InvalidRequestException;
//import com.pestov.testexercise.repositories.UserRepository;
//import com.pestov.testexercise.services.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.view.RedirectView;
//
//@Controller
//@RequestMapping(value = "login")
//public class LoginController {
//
//	@Autowired
//	private IUserService userService;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseBody
//	public RedirectView login(@RequestBody String request, BindingResult bindingResult) {
//		UserDto userCredentials = new UserDto(request);
//		if (userRepository.findByEmail(userCredentials.getEmail()) == null) {
//			bindingResult.reject("email", ApiError.USER_NOT_FOUND.errDesc);
//			throw new InvalidRequestException(bindingResult);
//		}
//		if (userRepository.findByEmail(userCredentials.getEmail()).getPassword().equals(userCredentials.getPassword())) {
//			return new RedirectView("/homepage.html");
//		} else {
//			return new RedirectView("/login.html");
//		}
//	}
//}
