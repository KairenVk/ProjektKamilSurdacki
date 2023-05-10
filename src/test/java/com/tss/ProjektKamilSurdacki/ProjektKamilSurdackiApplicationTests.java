package com.tss.ProjektKamilSurdacki;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.data.User;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.data.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@SpringBootTest
class ProjektKamilSurdackiApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CredentialsRepository credentialsRepository;
	private static User newUser;

	@BeforeAll
	public static void initializeDataObjects(){
		newUser = new User();
		newUser.setId(1L);
		newUser.setUsername("test");
	}

	@Test
	public void shouldSaveUserToDataDB() {
		userRepository.save(newUser);
		Optional<User> userId = userRepository.findById(1L);
		assert(userId.isPresent());
	}

	@Test
	public void shouldSaveCredentialsToCredentialsDB() {
		Credentials newCredentials = new Credentials();
		newCredentials.setId(1L);
		newCredentials.setUser_id(1L);
		newCredentials.setLogin("test");
		newCredentials.setPassword("password");
		newCredentials.setEmail("test@mail.com");
		newCredentials.setActive(true);
		credentialsRepository.save(newCredentials);
		Optional<Credentials> credentialsId = credentialsRepository.findById(1L);
		assert(credentialsId.isPresent());
	}

}
