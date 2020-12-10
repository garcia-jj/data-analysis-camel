package br.com.otavio.data.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class DataAnalysisApplication {

	public static void main(final String[] args) {
		SpringApplication.run(DataAnalysisApplication.class, args);
	}
}