package com.baduk.baduk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableJpaAuditing
@EnableDiscoveryClient
public class DongdaemunkillerApplication {
	public static void main(String[] args){
		SpringApplication.run(DongdaemunkillerApplication.class, args);
	}
}