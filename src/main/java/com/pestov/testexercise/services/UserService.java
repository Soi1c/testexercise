package com.pestov.testexercise.services;

import com.pestov.testexercise.dto.BookSharingDto;
import com.pestov.testexercise.dto.UserDto;
import com.pestov.testexercise.models.BookSharing;
import com.pestov.testexercise.models.CustomUser;
import com.pestov.testexercise.repositories.BookSharingRepository;
import com.pestov.testexercise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pestov.testexercise.conf.JWTAuthorizationFilter.getLoggedUserId;


@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;

	private final IRegTokenService regTokenService;

	private final IEmailService emailService;

	private final BookSharingRepository bookSharingRepository;

	public UserService(UserRepository userRepository, IRegTokenService regTokenService, IEmailService emailService, BookSharingRepository bookSharingRepository) {
		this.userRepository = userRepository;
		this.regTokenService = regTokenService;
		this.emailService = emailService;
		this.bookSharingRepository = bookSharingRepository;
	}

	@Value("${spring.application.url}")
	private String applicationUrl;

	@Override
	@Transactional
	public CustomUser registerNewUser(String userDto) {
		final CustomUser customUser = new CustomUser();
		UserDto dto = new UserDto(userDto);
		customUser.setEmail(dto.getEmail());
		customUser.setPassword(dto.getPassword());
		userRepository.save(customUser);
		String token = regTokenService.saveNewRegToken(customUser);
		emailService.sendSimpleMessage(
				dto.getEmail(),
				"Подтверждение регистрации",
				applicationUrl.concat("/signup/confirmEmail?token=").concat(token)
		);
		return customUser;
	}

	public List<CustomUser> getUsers() {
		return userRepository.findAll();
	}

	public BookSharing createBookSharingRequest(BookSharingDto bookSharingDto) {
		BookSharing bookSharing;
		if (bookSharingDto.getExpireDate() == null) {
			bookSharing = new BookSharing(bookSharingDto.getOwnerUserId(), bookSharingDto.getAskingUserId(),
					bookSharingDto.getBook_id());
		} else {
			bookSharing = new BookSharing(bookSharingDto.getOwnerUserId(), bookSharingDto.getAskingUserId(),
					bookSharingDto.getExpireDate(), bookSharingDto.getBook_id());
		}
		bookSharingRepository.save(bookSharing);
		return bookSharing;
	}

	public List<BookSharing> getMyRequests() {
		return bookSharingRepository.findAllByOwnerUserIdAndAllowedIsFalse(getLoggedUserId());
	}


}
