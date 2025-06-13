package com.program.course_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.program.course_service")
@EnableDiscoveryClient
public class CourseServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}
}
