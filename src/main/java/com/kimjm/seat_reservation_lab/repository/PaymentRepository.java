package com.kimjm.seat_reservation_lab.repository;

import com.kimjm.seat_reservation_lab.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}