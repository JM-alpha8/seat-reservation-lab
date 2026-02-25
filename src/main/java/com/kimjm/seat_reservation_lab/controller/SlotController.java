package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.dto.SlotDto;
import com.kimjm.seat_reservation_lab.service.SlotQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SlotController {

    private final SlotQueryService slotQueryService;

    @GetMapping("/venues/{venueId}/slots")
    public List<SlotDto> getSlots(@PathVariable Long venueId) {
        return slotQueryService.getSlotsByVenue(venueId);
    }

    @GetMapping("/slots")
    public List<SlotDto> getAllSlots() {
        return slotQueryService.getAllSlots();
    }
}