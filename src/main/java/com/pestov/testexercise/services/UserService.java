package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.mapper.Mappers;
import com.pestov.testexercise.models.Book;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.BookRepository;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final IRegTokenService regTokenService;
	private final IEmailService emailService;
	private final BookSharingRepository bookSharingRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final BookRepository bookRepository;
	private final Mappers mappers;


	public UserService(UserRepository userRepository,
					   IRegTokenService regTokenService,
					   IEmailService emailService,
					   BookSharingRepository bookSharingRepository,
					   BCryptPasswordEncoder bCryptPasswordEncoder,
					   BookRepository bookRepository,
					   Mappers mappers) {
		this.userRepository = userRepository;
		this.regTokenService = regTokenService;
		this.emailService = emailService;
		this.bookSharingRepository = bookSharingRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.bookRepository = bookRepository;
		this.mappers = mappers;
	}

	@Value("${spring.application.url}")
	private String applicationUrl;

	@Override
	@Transactional
	public CustomUser registerNewUser(UserDto userDto) {
		final CustomUser customUser = new CustomUser();
		customUser.setEmail(userDto.getEmail());
		assert userDto.getPassword() != null;
		customUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userRepository.save(customUser);
		String token = regTokenService.saveNewRegToken(customUser);
		emailService.sendSimpleMessage(
				userDto.getEmail(),
				"Подтверждение регистрации",
				applicationUrl.concat("/signup/confirmEmail?token=").concat(token)
		);
		assert userDto.getEmail() != null;
		log.info("Email sent to ".concat(userDto.getEmail()));
		return customUser;
	}

	public List<UserDto> getUsers(Long customUserId) {
		List<CustomUser> usersList = userRepository.findAll();
		List<UserDto> usersDtoList = new ArrayList<>();
		for (CustomUser user : usersList) {
			if (user.getId().equals(customUserId) || !user.isActive()) {
				continue;
			}
			UserDto userDto = new UserDto();
			userDto.setEmail(user.getEmail());
			userDto.setId(user.getId());
			usersDtoList.add(userDto);
		}
		return usersDtoList;
	}

	public void createBookSharingRequest(BookSharingDto bookSharingDto, Long customUserId) {
		CustomUser owner = userRepository.getOne(bookSharingDto.getOwnerUserId());
		CustomUser asker = userRepository.getOne(customUserId);
		Book book = bookRepository.getOne(bookSharingDto.getBook_id());
		BookSharing bookSharing = new BookSharing(owner, asker, book);
		bookSharingRepository.save(bookSharing);
	}

	public List<BookSharingDto> getMyRequests(Long customUserId) {
		List<BookSharing> myRequests = bookSharingRepository.findAllByOwnerUserIdAndAllowedIsFalse(customUserId);
		List<BookSharingDto> myRequestsDto = new ArrayList<>();
		for (BookSharing bookSharing: myRequests) {
			if (bookSharing.isRefused()) {
				continue;
			}
			BookSharingDto booksharingDto = new BookSharingDto();
			booksharingDto.setAskingUsername(bookSharing.getAskingUser().getEmail());
			booksharingDto.setBookName(bookSharing.getBook().getName());
			booksharingDto.setBookshelfName(bookSharing.getBook().getBookshelf().getName());
			booksharingDto.setId(bookSharing.getId());
			myRequestsDto.add(booksharingDto);
		}
		return myRequestsDto;
	}

	public BookSharingDto allowBooksharingRequestById(Long booksharingId, LocalDate expireDate) {
		BookSharing bookSharing = bookSharingRepository.getOne(booksharingId);
		bookSharing.setAllowed(true);
		if (expireDate != null) {
			bookSharing.setExpireDate(expireDate);
		}
		bookSharingRepository.save(bookSharing);
		BookSharingDto bookSharingDto = new BookSharingDto();
		mappers.getBooksharingMapper().map(bookSharing, bookSharingDto);
		return bookSharingDto;
	}

	public BookSharingDto refuseBooksharingRequestById(Long booksharingId, BookSharingDto bookSharingDto) {
		BookSharing bookSharing = bookSharingRepository.getOne(booksharingId);
		bookSharing.setAllowed(false);
		bookSharing.setRefuseDescription(bookSharingDto.getRefuseDescription());
		bookSharing.setRefused(true);
		bookSharingRepository.save(bookSharing);
		mappers.getBooksharingMapper().map(bookSharing, bookSharingDto);
		return bookSharingDto;
	}

	public void deleteExpiredBooksharings(LocalDate yesterday) {
		List<BookSharing> expiredList = bookSharingRepository.findAllByExpireDateEquals(yesterday);
		for (BookSharing bookSharing : expiredList) {
			bookSharing.setAllowed(false);
			bookSharingRepository.save(bookSharing);
		}
	}

	public List<BookSharingDto> myRefusedRequests(Long customUserId) {
		List<BookSharing> bookSharings = bookSharingRepository.findAllByAskingUserIdAndRefusedIsTrue(customUserId);
		List<BookSharingDto> bookSharingDtos = new ArrayList<>();
		for (BookSharing bookSharing : bookSharings) {
			bookSharingDtos.add(mappers.getBooksharingMapper().map(bookSharing, new BookSharingDto()));
		}
		return bookSharingDtos;
	}

	public List<BookSharingDto> mySharedBooks(Long customUserId) {
		List<BookSharing> bookSharings =  bookSharingRepository.findAllByAskingUserIdAndAllowedIsTrue(customUserId);
		List<BookSharingDto> bookSharingDtos = new ArrayList<>();
		for (BookSharing bookSharing : bookSharings) {
			bookSharingDtos.add(mappers.getBooksharingMapper().map(bookSharing, new BookSharingDto()));
		}
		return bookSharingDtos;
	}

	public boolean checkBookShared(Long bookId, Long customUserId) {
		return bookSharingRepository.findByAskingUserIdAndBookId(customUserId, bookId).isAllowed();
	}

	public BookSharing findBooksharingByAskingUserIdAndBookId(Long customUserId, Long bookId) {
		return bookSharingRepository.findByAskingUserIdAndBookId(customUserId, bookId);
	}

	public Boolean isRequestAlreadySent(Long bookId, Long customUserId) {
		return (bookSharingRepository.findByAskingUserIdAndBookIdAndRefusedIsFalse(customUserId, bookId) != null
				|| bookSharingRepository.findByAskingUserIdAndBookIdAndExpireDateBefore(customUserId, bookId, LocalDate.now()) != null);
	}

	public Boolean isEmailAlreadyExists(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	public BookSharing findBooksharingById(Long booksharingId) {
		return bookSharingRepository.getOne(booksharingId);
	}
}
