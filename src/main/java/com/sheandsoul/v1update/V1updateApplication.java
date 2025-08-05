package com.sheandsoul.v1update;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class V1updateApplication {

	public static void main(String[] args) {
		log.info("Starting V1update Application...");
		log.info("PORT environment variable: {}", System.getenv("PORT"));
		log.info("DATABASE_URL environment variable: {}", System.getenv("DATABASE_URL") != null ? "[REDACTED]" : "null");
		log.info("SPRING_PROFILES_ACTIVE: {}", System.getenv("SPRING_PROFILES_ACTIVE"));
		
		try {
			SpringApplication.run(V1updateApplication.class, args);
		} catch (Exception e) {
			log.error("Failed to start application", e);
			throw e;
		}
	}

}
