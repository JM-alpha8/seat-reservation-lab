package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.dto.SeatDto;
import com.kimjm.seat_reservation_lab.service.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SeatController {

    private final SeatQueryService seatQueryService;

    @GetMapping("/slots/{slotId}/seats")
    public List<SeatDto> getSeats(@PathVariable Long slotId) {
        return seatQueryService.getSeatsBySlotId(slotId);
    }
}