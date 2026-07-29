package com.retrobazar;

import org.springframework.boot.SpringApplication;

public class TestRetroBazarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(RetroBazarBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
