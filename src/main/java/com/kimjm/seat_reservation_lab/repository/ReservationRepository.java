package com.kimjm.seat_reservation_lab.repository;

import com.kimjm.seat_reservation_lab.entity.Reservation;
import com.kimjm.seat_reservation_lab.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 같은 좌석이 이미 HOLD/CONFIRMED 상태인지 확인 (락 없이 1차)
    Optional<Reservation> findFirstBySlotIdAndSeatIdAndStatusInOrderByCreatedAtDesc(
            Long slotId, Long seatId, java.util.List<ReservationStatus> statuses
    );

    // 만료된 HOLD 찾기(나중에 스케줄러에서 사용)
    long countByStatusAndHoldExpiresAtBefore(ReservationStatus status, LocalDateTime time);
}