package com.hhpl.concertreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ConcertReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertReservationServiceApplication.class, args);
	}

}
