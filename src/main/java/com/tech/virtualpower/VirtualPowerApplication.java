package com.tech.virtualpower;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class VirtualPowerApplication {
	private static final Logger logger = LoggerFactory.getLogger(VirtualPowerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(VirtualPowerApplication.class, args);
		logger.info("Spring boot application is started");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
