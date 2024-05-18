package com.example.simpleDropbox;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.simpleDropbox.properties.StorageProperties;
import com.example.simpleDropbox.service.MObjectStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SimpleDropboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleDropboxApplication.class, args);
	}

	@Bean
	CommandLineRunner init(MObjectStorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
