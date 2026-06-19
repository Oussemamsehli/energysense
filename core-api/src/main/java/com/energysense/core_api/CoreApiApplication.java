package com.energysense.core_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class CoreApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreApiApplication.class, args);
	}
}