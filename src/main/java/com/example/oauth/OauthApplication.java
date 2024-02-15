package com.example.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan("com.example.oauth")
@SpringBootApplication
public class OauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthApplication.class, args);
	}

}
