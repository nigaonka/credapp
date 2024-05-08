package com.ng.credit.creditapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
public class CreditappApplication {

	static {
		// Both lines need to be here.  Ignore spring warning in logs about logback.configurationFile
		System.setProperty("logback.configurationFile", System.getenv("LOGBACK_CONFIG"));
		System.setProperty("logging.config", "classpath:/" + System.getenv("LOGBACK_CONFIG"));
	}


	public static void main(String[] args)
	{

		System.out.println(" Starting the Credit App ");
		SpringApplication.run(CreditappApplication.class, args);
	}

}
