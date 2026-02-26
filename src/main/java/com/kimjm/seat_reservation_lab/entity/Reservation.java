package com.kimjm.seat_reservation_lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservation",
        indexes = {
                @Index(name = "idx_reservation_slot", columnList = "slot_id"),
                @Index(name = "idx_reservation_user", columnList = "user_id"),
                @Index(name = "idx_reservation_status", columnList = "status")
        }
)
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservation_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservation_slot"))
    private Slot slot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservation_seat"))
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    // HOLD 만료 시간
    private LocalDateTime holdExpiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // === 도메인 메서드(나중에 본격 사용) ===
    public void confirm() {
        if (this.status != ReservationStatus.HOLD) {
            throw new IllegalStateException("Only HOLD can be confirmed");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == ReservationStatus.EXPIRED) {
            throw new IllegalStateException("Expired reservation cannot be cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (this.status != ReservationStatus.HOLD) {
            throw new IllegalStateException("Only HOLD can expire");
        }
        this.status = ReservationStatus.EXPIRED;
    }
}