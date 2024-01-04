package com.example.virtuallibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories("com.example.virtuallibrary")
@EntityScan("com.example.virtuallibrary")
@SpringBootApplication
public class VirtualLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualLibraryApplication.class, args);
	}

}
