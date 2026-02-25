package com.kimjm.seat_reservation_lab.dto;

import lombok.Builder;

@Builder
public record SeatDto(
        Long seatId,
        String seatCode
) {}