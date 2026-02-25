package com.kimjm.seat_reservation_lab.dto;

import jakarta.validation.constraints.NotNull;

public record HoldRequest(
        Long slotId,
        Long seatId
) {}