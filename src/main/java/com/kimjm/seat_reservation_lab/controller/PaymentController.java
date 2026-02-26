package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.dto.ConfirmPaymentRequest;
import com.kimjm.seat_reservation_lab.dto.ConfirmPaymentResponse;
import com.kimjm.seat_reservation_lab.service.PaymentCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentCommandService paymentCommandService;

    @PostMapping("/confirm")
    public ConfirmPaymentResponse confirm(@RequestBody @Valid ConfirmPaymentRequest req) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return paymentCommandService.confirm(userId, req);
    }
}