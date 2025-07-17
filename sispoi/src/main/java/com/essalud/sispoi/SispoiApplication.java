package com.essalud.sispoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.essalud.sispoi.model")
public class SispoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SispoiApplication.class, args);
	}

}
