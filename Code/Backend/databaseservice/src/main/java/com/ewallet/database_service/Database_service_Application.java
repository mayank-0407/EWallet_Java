package com.ewallet.database_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Database_service_Application {

	public static void main(String[] args) {
		SpringApplication.run(Database_service_Application.class, args);
	}
}