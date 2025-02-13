package com.rest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.calculator.demo","com.rest.demo", "com.example.kafka"})
public class JavaChallenge2024Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaChallenge2024Application.class, args);
	}

}
