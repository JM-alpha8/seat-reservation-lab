package com.kimjm.seat_reservation_lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_payment_reservation",
                columnNames = "reservation_id"
        )
)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payment_reservation"))
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = PaymentStatus.INIT;
    }

    public void success() { this.status = PaymentStatus.SUCCESS; }
    public void fail()    { this.status = PaymentStatus.FAILED; }
}