package com.kimjm.seat_reservation_lab.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ConfirmPaymentRequest(
        @NotNull Long reservationId,
        @Min(0) int amount
) {}