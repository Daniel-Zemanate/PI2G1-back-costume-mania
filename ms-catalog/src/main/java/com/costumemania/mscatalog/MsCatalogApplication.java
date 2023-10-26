package com.costumemania.mscatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class MsCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCatalogApplication.class, args);
	}

}
