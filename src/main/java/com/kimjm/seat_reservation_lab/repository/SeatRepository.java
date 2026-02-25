package com.kimjm.seat_reservation_lab.repository;

import com.kimjm.seat_reservation_lab.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from Seat s
        where s.id = :seatId
    """)
    Optional<Seat> findByIdForUpdate(Long seatId);

    @Query("""
        select s
        from Seat s
        where s.venue.id = :venueId
        order by s.seatCode asc
    """)
    List<Seat> findAllByVenueId(Long venueId);
}