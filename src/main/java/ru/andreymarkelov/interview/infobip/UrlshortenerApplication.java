package ru.andreymarkelov.interview.infobip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.andreymarkelov.interview.infobip")
public class UrlshortenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrlshortenerApplication.class, args);
	}
}
