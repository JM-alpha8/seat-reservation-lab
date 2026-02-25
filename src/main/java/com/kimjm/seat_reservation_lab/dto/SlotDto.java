package com.kimjm.seat_reservation_lab.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SlotDto(
        Long slotId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String status,
        String venueName
) {}