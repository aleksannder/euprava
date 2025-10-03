package com.github.aleksannder.zavodzastatistiku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableFeignClients
@EnableMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class ZavodZaStatistikuApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZavodZaStatistikuApplication.class, args);
	}

}
