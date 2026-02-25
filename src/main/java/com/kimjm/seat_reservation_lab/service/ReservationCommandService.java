package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.dto.HoldRequest;
import com.kimjm.seat_reservation_lab.dto.HoldResponse;
import com.kimjm.seat_reservation_lab.entity.*;
import com.kimjm.seat_reservation_lab.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationCommandService {

    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HoldResponse hold(HoldRequest req) {

        Long userId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Slot slot = slotRepository.findById(req.slotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + req.slotId()));
        Seat seat = seatRepository.findByIdForUpdate(req.seatId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + req.seatId()));

        // ⚠️ 여기서 동시성 터짐 포인트: "조회 후 생성" (check-then-act)
        reservationRepository.findFirstBySlotIdAndSeatIdAndStatusInOrderByCreatedAtDesc(
                slot.getId(), seat.getId(),
                List.of(ReservationStatus.HOLD, ReservationStatus.CONFIRMED)
        ).ifPresent(r -> {
            // HOLD가 만료됐으면 통과시키는 등 로직을 나중에 추가
            throw new IllegalStateException("Seat already held/confirmed");
        });

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(3);

        Reservation saved = reservationRepository.save(Reservation.builder()
                .user(user)
                .slot(slot)
                .seat(seat)
                .status(ReservationStatus.HOLD)
                .holdExpiresAt(expiresAt)
                .build());

        return new HoldResponse(saved.getId(), saved.getStatus().name(), saved.getHoldExpiresAt());
    }
}