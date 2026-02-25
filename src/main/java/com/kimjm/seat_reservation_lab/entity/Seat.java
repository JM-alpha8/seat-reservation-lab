package com.kimjm.seat_reservation_lab.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seat",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_seat_venue_code",
                columnNames = {"venue_id", "seat_code"}
        )
)
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_seat_venue"))
    private Venue venue;

    @Column(name = "seat_code", nullable = false, length = 10)
    private String seatCode;
}