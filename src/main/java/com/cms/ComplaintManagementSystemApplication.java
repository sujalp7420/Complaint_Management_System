package com.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class ComplaintManagementSystemApplication {

	public static void main(String[] args) {
        SpringApplication.run(ComplaintManagementSystemApplication.class, args);
        System.out.println("System started");
	}

}
