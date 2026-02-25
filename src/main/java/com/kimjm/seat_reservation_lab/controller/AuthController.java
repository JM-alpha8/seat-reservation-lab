package com.kimjm.seat_reservation_lab.controller;

import com.kimjm.seat_reservation_lab.dto.LoginRequest;
import com.kimjm.seat_reservation_lab.dto.LoginResponse;
import com.kimjm.seat_reservation_lab.dto.SignupRequest;
import com.kimjm.seat_reservation_lab.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest req) {
        authService.signup(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
