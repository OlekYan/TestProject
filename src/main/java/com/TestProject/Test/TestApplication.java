package com.TestProject.Test;

import Things.Alien;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.TestProject.Test", "Things"})
public class TestApplication {

	public static void main(String[] args) {
	ApplicationContext context =	SpringApplication.run(TestApplication.class, args);
		Alien alien = context.getBean(Alien.class);
		alien.code();



	}
}
