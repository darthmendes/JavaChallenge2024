package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.example.rest_api_module","com.example.calculator_module"})
public class JavaChallenge2024Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaChallenge2024Application.class, args);
	}
}