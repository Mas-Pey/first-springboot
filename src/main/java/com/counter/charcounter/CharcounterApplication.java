package com.counter.charcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class CharcounterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CharcounterApplication.class, args);
	}
}
