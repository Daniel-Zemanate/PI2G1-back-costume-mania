package com.costumemania.msfacturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class MsFacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFacturacionApplication.class, args);
	}

}
