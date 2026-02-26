package com.kimjm.seat_reservation_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SeatReservationLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatReservationLabApplication.class, args);
	}

}
