package com.sheandsoul.v1update.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ServerConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    
    @Value("${PORT:8080}")
    private String port;
    
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        int portNumber = Integer.parseInt(port);
        log.info("Setting server port to: {}", portNumber);
        factory.setPort(portNumber);
    }
}
