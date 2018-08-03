package com.pestov.testexercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.pestov.testexercise.models.CustomUser;
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

	public FixtureLoader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		loadFixtures();
	}

	public void loadFixtures() {
		loadUsers();
	}

	@Transactional
	public void loadUsers() {
		userRepository.deleteAllInBatch();
		List<CustomUser> customUsers = loadFromJson("fixtures/users.json", CustomUser.class);
		//customUsers.get(0).setRoles(Collections.singletonList(roleRepository.findByName("ROLE_ADMIN")));

//		customUsers.forEach(u -> {
//			List<Role> roles = new ArrayList<>();
//			u.getRoles().forEach(r -> {
//				Role byName = roleRepository.findByName(r.getName());
//				roles.add(byName);
//			});
//			u.setRoles(roles);
//		});
//		customUsers.forEach(a -> a.setPassword(passwordEncoder.encode(a.getPassword())));
		userRepository.saveAll(customUsers);
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
