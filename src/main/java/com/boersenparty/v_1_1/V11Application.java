package com.boersenparty.v_1_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class V11Application {

	public static void main(String[] args) {
		SpringApplication.run(V11Application.class, args);
	}

}
