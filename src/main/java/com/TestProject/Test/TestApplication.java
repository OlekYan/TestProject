package com.TestProject.Test;

import Things.Alien;
import Things.AlienP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.TestProject.Test", "Things"})
public class TestApplication {

	public static void main(String[] args) {
	ApplicationContext context =	SpringApplication.run(TestApplication.class, args);
		Alien alien1 = context.getBean(Alien.class);
		alien1.code();
		alien1.age = 10;
		System.out.println(alien1.age);

		Alien alien2 = context.getBean(Alien.class);
		alien2.code();

		System.out.println(alien2.age);

		AlienP alienP1  = context.getBean(AlienP.class);
		alienP1.setAge(3);
		System.out.println(alienP1.getAge());


	}
}
