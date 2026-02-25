package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.dto.HoldRequest;
import com.kimjm.seat_reservation_lab.dto.HoldResponse;
import com.kimjm.seat_reservation_lab.service.ReservationCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;

    @PostMapping("/hold")
    public HoldResponse hold(@RequestBody @Valid HoldRequest req) {
        return reservationCommandService.hold(req);
    }
}