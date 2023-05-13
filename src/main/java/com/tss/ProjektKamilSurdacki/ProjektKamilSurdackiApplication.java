package com.tss.ProjektKamilSurdacki;

import com.tss.entities.credentials.Credentials;
import com.tss.entities.credentials.Users_roles;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.credentials.Users_rolesRepository;
import com.tss.repositories.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages={"com.tss"})
public class ProjektKamilSurdackiApplication {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	CredentialsRepository credentialsRepository;

	@Autowired
	Users_rolesRepository users_rolesRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjektKamilSurdackiApplication.class, args);
	}
//	@Bean
//	CommandLineRunner addUser() {
//		return (args) -> {
//			Credentials admin = new Credentials();
//			admin.setLogin("admin");
//			admin.setPassword(bCryptPasswordEncoder.encode("admin"));
//			admin.setUser_id(3L);
//			admin.setActive(true);
//			admin.setEmail("admin@mail.com");
//			credentialsRepository.save(admin);
//			Users_roles adminRole = new Users_roles();
//			adminRole.setUser_role("ADMIN");
//			adminRole.setLogin("admin");
//			users_rolesRepository.save(adminRole);
//		};
//	}
}
