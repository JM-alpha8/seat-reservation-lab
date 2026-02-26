package com.kimjm.seat_reservation_lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, length = 50)
    private String method; // MOCK_CARD 등

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @PrePersist
    void onCreate() {
        if (paidAt == null) paidAt = LocalDateTime.now();
    }
}