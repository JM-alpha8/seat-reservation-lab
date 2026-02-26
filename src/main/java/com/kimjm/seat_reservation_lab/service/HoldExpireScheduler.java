package com.kimjm.seat_reservation_lab.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HoldExpireScheduler {

    private final HoldExpireService holdExpireService;

    // 30초마다 만료 처리 (원하면 10초/60초로 조절)
    @Scheduled(fixedDelay = 300_000)
    public void expireTask() {
        int expired = holdExpireService.expireNow();
        if (expired > 0) {
            log.info("Expired holds: {}", expired);
        }
    }
}