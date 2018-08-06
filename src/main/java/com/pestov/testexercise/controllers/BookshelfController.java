package com.pestov.testexercise.controllers;


import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.services.BookshelfService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;

@Controller
@RequestMapping(value = "bookshelf")
public class BookshelfController {

	@Autowired
	private BookshelfService bookshelfService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity submit(@RequestBody BookshelfDto request) {
		bookshelfService.saveNewBookshelf(getLoggedUserId(), request);
		JSONObject response = new JSONObject().put("status", "ok");
		return new ResponseEntity(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Bookshelf>> list() {
		return new ResponseEntity<>(bookshelfService.bookshelvesByUser(getLoggedUserId()), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity deleteBookshelf(@RequestBody Long id) {
		bookshelfService.deleteBookshelf(id);
		JSONObject response = new JSONObject().put("status", "ok");
		return new ResponseEntity(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity renameBookshelf(@RequestBody BookshelfDto bookshelfDto) {
		bookshelfService.renameBookshelf(bookshelfDto);
		JSONObject response = new JSONObject().put("status", "ok");
		return new ResponseEntity(response.toString(), HttpStatus.OK);
	}

}
