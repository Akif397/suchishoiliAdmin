package com.example.suchishoiliWeb.suchishoili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
@EnableWebSecurity
//@ComponentScan(basePackages = {"com.example.suchishoiliWeb.suchishoili.controller"})
public class SuchishoiliApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuchishoiliApplication.class, args);
		System.out.println("Welcome to Suchishoili Admin");
//		System.out.println("version: " + SpringVersion.getVersion());
	}

}
