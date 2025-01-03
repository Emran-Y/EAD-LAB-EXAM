package com.todo.OnlineBookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ServletComponentScan // Enables scanning for @WebServlet annotations
@ImportResource("classpath:applicationContext.xml") // Loads XML-based beans
public class TodoTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoTestApplication.class, args);
	}
}
