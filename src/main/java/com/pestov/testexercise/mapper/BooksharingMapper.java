package com.pestov.testexercise.mapper;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.models.BookSharing;
import org.springframework.stereotype.Component;

@Component
public class BooksharingMapper extends AbstractMapper<BookSharing, BookSharingDto> {

	@Override
	public BookSharingDto map(BookSharing source, BookSharingDto target) {
		if (source == null) return null;
		if (target == null) target = new BookSharingDto();

		target.setId(source.getId());
		target.setBookName(source.getBook().getName());
		target.setAskingUsername(source.getAskingUser().getEmail());
		target.setBookshelfName(source.getBook().getBookshelf().getName());
		target.setAllowed(source.isAllowed());
		target.setAskingUserId(source.getAskingUser().getId());
		target.setBook_id(source.getBook().getId());
		target.setOwnerUserId(source.getOwnerUser().getId());
		target.setExpireDate(source.getExpireDate());
		target.setRefuseDescription(source.getRefuseDescription());
		target.setRefused(source.isRefused());

		return target;
	}
}
