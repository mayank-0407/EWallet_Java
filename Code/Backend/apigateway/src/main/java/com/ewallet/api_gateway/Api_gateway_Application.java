package com.ewallet.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Api_gateway_Application {

	public static void main(String[] args) {
		SpringApplication.run(Api_gateway_Application.class, args);
	}

}
