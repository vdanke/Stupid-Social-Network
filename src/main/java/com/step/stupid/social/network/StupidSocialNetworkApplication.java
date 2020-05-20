package com.step.stupid.social.network;

import com.step.stupid.social.network.configuration.AppProperties;
import com.step.stupid.social.network.model.Role;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(value = {AppProperties.class})
@Slf4j
public class StupidSocialNetworkApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository) {
		return args -> {
			User first = User.builder()
					.username("firstgooduser@mail.ru")
					.password(passwordEncoder.encode("firstuser"))
					.authorities(Collections.singleton(Role.ROLE_USER))
					.build();
			User second = User.builder()
					.username("secondgooduser@mail.ru")
					.password(passwordEncoder.encode("seconduser"))
					.authorities(Collections.singleton(Role.ROLE_ADMIN))
					.build();
			userRepository.saveAll(Arrays.asList(first, second)).forEach(user -> {
				log.info(user.getId().toString());
			});
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(StupidSocialNetworkApplication.class, args);
	}

}
