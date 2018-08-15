package com.pestov.testexercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

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

	public void loadFixtures() {
		loadUsers();
		loadBookshelves();
		loadBooks();
	}

	@Transactional
	public void loadUsers() {
		userRepository.deleteAllInBatch();
		List<CustomUser> customUsers = loadFromJson("fixtures/users.json", CustomUser.class);
		userRepository.saveAll(customUsers);
		log.info(String.valueOf(customUsers.size()).concat(" users loaded from fixture"));
	}

	@Transactional
	public void loadBookshelves() {
		bookshelfRepository.deleteAllInBatch();
		List<Bookshelf> bookshelves = loadFromJson("fixtures/bookshelves.json", Bookshelf.class);
		bookshelfRepository.saveAll(bookshelves);
		log.info(String.valueOf(bookshelves.size()).concat(" bookshelves loaded from fixture"));
	}

	@Transactional
	public void loadBooks() {
		bookRepository.deleteAllInBatch();
		List<Book> books = loadFromJson("fixtures/books.json", Book.class);
		bookRepository.saveAll(books);
		log.info(String.valueOf(books.size()).concat(" books loaded from fixture"));
	}

	public <T> T loadFromJson(String resourcePath, Class<?> target) {
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream resource = getClass().getClassLoader().getResourceAsStream(resourcePath);

		try (Reader reader = new InputStreamReader(resource, UTF_8)) {
			final Class<?> elementClass = Class.forName(target.getName());
			final TypeFactory typeFactory = objectMapper.getTypeFactory();
			final CollectionType valueType = typeFactory.constructCollectionType(List.class, elementClass);
			return objectMapper.readValue(reader, valueType);
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException("Ошибка при загрузке " + resourcePath + ": " + e.getMessage());
		}
	}
}
