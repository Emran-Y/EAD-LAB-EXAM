package com.todo.OnlineBookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

// Emran Yonas Yimer

@SpringBootApplication
@ServletComponentScan // Enables scanning for @WebServlet annotations
@ImportResource("classpath:applicationContext.xml") // Loads XML-based beans
public class BookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}
}
