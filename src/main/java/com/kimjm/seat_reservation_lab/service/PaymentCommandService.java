package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.dto.ConfirmPaymentRequest;
import com.kimjm.seat_reservation_lab.dto.ConfirmPaymentResponse;
import com.kimjm.seat_reservation_lab.entity.*;
import com.kimjm.seat_reservation_lab.repository.PaymentRepository;
import com.kimjm.seat_reservation_lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentCommandService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ConfirmPaymentResponse confirm(Long currentUserId, ConfirmPaymentRequest req) {

        Reservation r = reservationRepository.findByIdForUpdate(req.reservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // 1) 본인 예약인지
        if (!r.getUser().getId().equals(currentUserId)) {
            throw new IllegalStateException("Not your reservation");
        }

        // 2) 상태 체크
        if (r.getStatus() != ReservationStatus.HOLD) {
            throw new IllegalStateException("Reservation is not HOLD");
        }

        // 3) 만료 체크
        if (r.getHoldExpiresAt() != null && r.getHoldExpiresAt().isBefore(LocalDateTime.now())) {
            r.expire(); // 엔티티 setter 필요 (또는 별도 메서드)
            throw new IllegalStateException("Hold expired");
        }

        // 4) 결제 생성(모의 결제: 항상 성공)
        Payment payment = paymentRepository.save(Payment.builder()
                .reservation(r)
                .status(PaymentStatus.PAID)
                .amount(req.amount())
                .method("MOCK_CARD")
                .paidAt(LocalDateTime.now())
                .build());

        // 5) 예약 확정
        r.confirm();

        return new ConfirmPaymentResponse(
                r.getId(),
                r.getStatus().name(),
                payment.getId(),
                payment.getStatus().name()
        );
    }
}