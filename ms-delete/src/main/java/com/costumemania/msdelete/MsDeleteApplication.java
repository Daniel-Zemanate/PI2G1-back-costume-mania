package com.costumemania.msdelete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsDeleteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDeleteApplication.class, args);
	}

}
