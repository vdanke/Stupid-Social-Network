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

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(StupidSocialNetworkApplication.class);

//		springApplication.setAdditionalProfiles("dev");

		springApplication.run(args);
	}

}
