package com.example.suchishoiliWeb.suchishoili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SuchishoiliApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuchishoiliApplication.class, args);
		System.out.println("hello spring world");
//		System.out.println("version: " + SpringVersion.getVersion());
	}

}
