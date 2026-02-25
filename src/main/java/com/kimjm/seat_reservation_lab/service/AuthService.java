package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.dto.LoginRequest;
import com.kimjm.seat_reservation_lab.dto.LoginResponse;
import com.kimjm.seat_reservation_lab.dto.SignupRequest;
import com.kimjm.seat_reservation_lab.entity.User;
import com.kimjm.seat_reservation_lab.repository.UserRepository;
import com.kimjm.seat_reservation_lab.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signup(SignupRequest req) {
        User user = User.builder()
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtProvider.generateToken(user.getId());
        return new LoginResponse(token);
    }

}
