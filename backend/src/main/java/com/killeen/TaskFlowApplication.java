package com.killeen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
	@PropertySource("classpath:exception.properties")
})
public class TaskFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskFlowApplication.class, args);
	}

}
