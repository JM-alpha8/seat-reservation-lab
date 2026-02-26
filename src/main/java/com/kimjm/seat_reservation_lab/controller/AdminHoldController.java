package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.service.HoldExpireService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/holds")
public class AdminHoldController {

    private final HoldExpireService holdExpireService;

    @PostMapping("/expire")
    public ExpireResult expire() {
        int affected = holdExpireService.expireNow();
        return new ExpireResult(affected);
    }

    public record ExpireResult(int expiredCount) {}
}