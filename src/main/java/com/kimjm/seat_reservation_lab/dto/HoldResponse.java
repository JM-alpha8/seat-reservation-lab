package com.kimjm.seat_reservation_lab.dto;

import java.time.LocalDateTime;

public record HoldResponse(
        Long reservationId,
        String status,
        LocalDateTime holdExpiresAt
) {}