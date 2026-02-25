package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.dto.SeatDto;
import com.kimjm.seat_reservation_lab.entity.Slot;
import com.kimjm.seat_reservation_lab.repository.SeatRepository;
import com.kimjm.seat_reservation_lab.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SeatQueryService {

    private final SlotRepository slotRepository;
    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<SeatDto> getSeatsBySlotId(Long slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        Long venueId = slot.getVenue().getId(); // LAZY 로딩 포인트 (JPA 체득)
        String venueName = slot.getVenue().getName();
        return seatRepository.findAllByVenueId(venueId).stream()
                .map(seat -> SeatDto.builder()
                        .seatId(seat.getId())
                        .seatCode(seat.getSeatCode())
                        .build())
                .toList();
    }
}