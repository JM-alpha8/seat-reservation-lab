package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.entity.ReservationStatus;
import com.kimjm.seat_reservation_lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class HoldExpireService {

    private final ReservationRepository reservationRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int expireNow() {
        return reservationRepository.expireHolds(
                LocalDateTime.now(),
                ReservationStatus.HOLD,
                ReservationStatus.EXPIRED
        );
    }
}