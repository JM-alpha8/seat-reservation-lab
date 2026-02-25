package com.kimjm.seat_reservation_lab.service;

import com.kimjm.seat_reservation_lab.dto.SlotDto;
import com.kimjm.seat_reservation_lab.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SlotQueryService {

    private final SlotRepository slotRepository;

    @Transactional(readOnly = true)
    public List<SlotDto> getSlotsByVenue(Long venueId) {
        return slotRepository.findAllByVenueIdOrderByStartAtAsc(venueId).stream()
                .map(slot -> SlotDto.builder()
                        .slotId(slot.getId())
                        .startAt(slot.getStartAt())
                        .endAt(slot.getEndAt())
                        .status(slot.getStatus().name())
                        // 여기서 venueName을 읽는 순간, slot 개수만큼 venue 쿼리 추가 가능
                        .venueName(slot.getVenue().getName())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SlotDto> getAllSlots() {
        return slotRepository.findAllWithVenueOrderByStartAt().stream()
                .map(slot -> SlotDto.builder()
                        .slotId(slot.getId())
                        .startAt(slot.getStartAt())
                        .endAt(slot.getEndAt())
                        .status(slot.getStatus().name())
                        .venueName(slot.getVenue().getName()) // N+1 유발 포인트
                        .build())
                .toList();
    }
}