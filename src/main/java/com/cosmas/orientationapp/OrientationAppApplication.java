package com.cosmas.orientationapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EntityScan(basePackages = "com.cosmas.model")
@EnableJpaRepositories(basePackages = "com.cosmas.orientationapp.repository")
public class OrientationAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrientationAppApplication.class, args);
	}

}
