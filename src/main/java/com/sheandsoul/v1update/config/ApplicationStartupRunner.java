package com.sheandsoul.v1update.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info("Application Started Successfully!");
        logger.info("Server is running on port: {}", serverPort);
        logger.info("Health check endpoint: http://localhost:{}/actuator/health", serverPort);
        logger.info("========================================");
    }
}
