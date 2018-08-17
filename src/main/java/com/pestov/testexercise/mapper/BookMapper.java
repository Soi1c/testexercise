package com.pestov.testexercise.mapper;


import com.pestov.testexercise.dto.BookDto;
import com.pestov.testexercise.models.Book;
import org.springframework.stereotype.Component;

@Component
public final class BookMapper extends AbstractMapper<Book, BookDto> {

	@Override
	public BookDto map(Book source, BookDto target) {
		if (source == null) return null;
		if (target == null) target = new BookDto();

		target.setBookshelfId(source.getBookshelf().getId());
		target.setDescription(source.getDescription());
		target.setId(source.getId());
		target.setName(source.getName());

		return target;
	}
}
