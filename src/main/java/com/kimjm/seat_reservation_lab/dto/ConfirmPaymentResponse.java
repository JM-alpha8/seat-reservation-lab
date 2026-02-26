package com.kimjm.seat_reservation_lab.dto;

public record ConfirmPaymentResponse(
        Long reservationId,
        String reservationStatus,
        Long paymentId,
        String paymentStatus
) {}