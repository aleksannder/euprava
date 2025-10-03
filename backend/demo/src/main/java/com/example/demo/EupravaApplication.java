package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableFeignClients
@EnableMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class EupravaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EupravaApplication.class, args);
	}

}
