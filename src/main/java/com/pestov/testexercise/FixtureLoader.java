package com.pestov.testexercise;

import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.Bookshelf;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookshelfRepository;
import com.pestov.testexercise.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class FixtureLoader implements ApplicationListener<ApplicationReadyEvent> {

	private final UserRepository userRepository;
	private final BookshelfRepository bookshelfRepository;
	private final BookRepository bookRepository;

	public FixtureLoader(UserRepository userRepository, BookshelfRepository bookshelfRepository, BookRepository bookRepository) {
		this.userRepository = userRepository;
		this.bookshelfRepository = bookshelfRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		loadFixtures();
	}

	@Transactional
	public void loadFixtures() {
		userRepository.deleteAllInBatch();
		bookshelfRepository.deleteAllInBatch();
		bookRepository.deleteAllInBatch();
		CustomUser customUser1 = new CustomUser("test@test.com", "$2a$10$/lZmy.ZMWCBmLlaw78/JaevmM.VjqQthF0T9avAunsVBE7019sIVy");
		CustomUser customUser2 = new CustomUser("test1@test.com", "$2a$10$/lZmy.ZMWCBmLlaw78/JaevmM.VjqQthF0T9avAunsVBE7019sIVy");
		CustomUser customUser3 = new CustomUser("test2@test.com", "$2a$10$/lZmy.ZMWCBmLlaw78/JaevmM.VjqQthF0T9avAunsVBE7019sIVy");
		CustomUser customUser4 = new CustomUser("test3@test.com", "$2a$10$/lZmy.ZMWCBmLlaw78/JaevmM.VjqQthF0T9avAunsVBE7019sIVy");
		List<CustomUser> customUsers = Stream.of(customUser1, customUser2, customUser3, customUser4)
				.peek(cu -> cu.setActive(true))
				.collect(Collectors.toList());
		userRepository.saveAll(customUsers);
		log.info(String.valueOf(customUsers.size()).concat(" users loaded from fixtures"));

		Bookshelf bookshelf1 = new Bookshelf(customUser2,"Первая полка пользователя test1");
		Bookshelf bookshelf2 = new Bookshelf(customUser2,"Вторая полка пользователя test1");
		Bookshelf bookshelf3 = new Bookshelf(customUser2,"Третья полка пользователя test1");
		List<Bookshelf> bookshelves = Arrays.asList(bookshelf1, bookshelf2, bookshelf3);
		bookshelfRepository.saveAll(bookshelves);
		log.info(String.valueOf(bookshelves.size()).concat(" bookshelves loaded from fixtures"));

		Book book1 = new Book(bookshelf1, "Первая книга первой полки юзера test1", "Я заебался фикстуры переделывать");
		Book book2 = new Book(bookshelf1, "Вторая книга первой полки юзера test1", "Убейте меня");
		List<Book> books = Arrays.asList(book1, book2);
		bookRepository.saveAll(books);
		log.info(String.valueOf(books.size()).concat(" books loaded from fixtures"));
	}
}
