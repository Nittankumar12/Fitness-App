package com.fitness.user_profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class UserProfileApplication{

	public static void main(String[] args) {
		SpringApplication.run(UserProfileApplication.class, args);
	}

}
