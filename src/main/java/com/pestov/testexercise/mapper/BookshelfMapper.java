package com.pestov.testexercise.mapper;

import com.pestov.testexercise.dto.BookshelfDto;
import com.pestov.testexercise.models.Bookshelf;
import org.springframework.stereotype.Component;

@Component
public class BookshelfMapper extends AbstractMapper<Bookshelf, BookshelfDto> {

	@Override
	public BookshelfDto map(Bookshelf source, BookshelfDto target) {
		if (target == null) return null;
		if (source == null) target = new BookshelfDto();

		target.setId(source.getId());
		target.setName(source.getName());

		return target;
	}
}
