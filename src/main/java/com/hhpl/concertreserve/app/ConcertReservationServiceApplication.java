package com.hhpl.concertreserve.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.hhpl.concertreserve.app", "com.hhpl.concertreserve.config"})
public class ConcertReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertReservationServiceApplication.class, args);
	}

}
