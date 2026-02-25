package com.kimjm.seat_reservation_lab.repository;

import com.kimjm.seat_reservation_lab.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findAllByVenueIdOrderByStartAtAsc(Long venueId);
    List<Slot> findAllByOrderByStartAtAsc();

    @Query("""
    select s
    from Slot s
    join fetch s.venue v
    order by s.startAt
""")
    List<Slot> findAllWithVenueOrderByStartAt();
}