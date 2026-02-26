# 동시성 문제

**Postman 사용하여 동시성 문제 유발**  
콜렉션 - run - Performance

```
@RequiredArgsConstructor
@Service
public class ReservationCommandService {

    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public HoldResponse hold(HoldRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId()));
        Slot slot = slotRepository.findById(req.slotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + req.slotId()));
        Seat seat = seatRepository.findById(req.seatId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + req.seatId()));

        // ⚠️ 여기서 동시성 터짐 포인트: "조회 후 생성" (check-then-act)
        reservationRepository.findFirstBySlotIdAndSeatIdAndStatusInOrderByCreatedAtDesc(
                slot.getId(), seat.getId(),
                List.of(ReservationStatus.HOLD, ReservationStatus.CONFIRMED)
        ).ifPresent(r -> {
            // HOLD가 만료됐으면 통과시키는 등 로직을 나중에 추가
            throw new IllegalStateException("Seat already held/confirmed");
        });

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(3);

        Reservation saved = reservationRepository.save(Reservation.builder()
                .user(user)
                .slot(slot)
                .seat(seat)
                .status(ReservationStatus.HOLD)
                .holdExpiresAt(expiresAt)
                .build());

        return new HoldResponse(saved.getId(), saved.getStatus().name(), saved.getHoldExpiresAt());
    }
}
```
---

동시성이 터지는 핵심은 “조회 후 저장(check-then-act)”가 원자적(atomic)이지 않기 때문

## 왜 터지나?

두 요청 A, B가 거의 동시에 들어오면:  
1. A 트랜잭션: findFirst... 조회 → “없음”  
2. B 트랜잭션: findFirst... 조회 → “없음” (A가 아직 저장 안 했거나, 저장했어도 아직 커밋 전이라 안 보임)  
3. A: save(HOLD)  
4. B: save(HOLD)  

→ 결과: 둘 다 HOLD를 만들어버림

---

## 해결 전략
- DB 제약 (최후 방어)
- Pessimistic Lock (DB 락)
- Optimistic Lock
- Redis 분산 락

---

## DB 제약
장점: 어떤 버그가 있어도 DB가 마지막에 막아줌  
단점: “HOLD는 중복 허용, CONFIRMED만 중복 금지” 같은 조건부 유니크가 MySQL에서 깔끔하지 않음

---

## Pessimistic Lock
좌석을 잡으려는 순간에 DB에서 Row Lock을 걸고, 같은 좌석을 잡으려는 다른 트랜잭션은 대기하게 만드는 방식.  

장점: 확실하게 막힘(중복 hold 거의 불가능)  
단점: 락 대기/데드락/성능 저하 가능


> SeatRepository에 PESSIMISTIC_WRITE 추가

```
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from Seat s
        where s.id = :seatId
    """)
    Optional<Seat> findByIdForUpdate(Long seatId);

    // 기존 findAllByVenueId는 그대로
}
```
> ReservationCommandService.hold() 에서 교체

```
Seat seat = seatRepository.findByIdForUpdate(req.seatId())
        .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + req.seatId()));
```
여기까지 했지만 트랜잭션 스냅샷이 '고정' 되어서  
reservation select 를 할 때 옛날 스냅샷을 참고하게 된다.  
(MySQL(InnoDB)은 기본이 보통 REPEATABLE READ 이다. 이게 문제를 만든다.)

> @Transactional(isolation = Isolation.READ_COMMITTED) 적용

READ COMMITTED는 “각 쿼리가 실행될 때마다” 최신 커밋을 보려고 하기 때문에,  
seat 락 대기 후에 실행되는 reservation 조회가 방금 커밋된 HOLD를 볼 확률이 확 올라간다.

---
## Optimistic Lock
충돌은 허용하되, @Version으로 “누가 먼저 수정했는지”를 감지하고  
충돌하면 예외(OptimisticLockException) → 재시도로 해결.

장점: 락 대기 없고 성능 좋음  
단점: 충돌 잦으면 재시도 폭발(예약/좌석은 충돌이 잦은 편)

---

## Redis 분산 락
좌석/슬롯 조합에 대해
lock:slot:{slotId}:seat:{seatId} 같은 키로 락을 잡고  
락 잡은 사람만 DB 트랜잭션에 들어가게 한다.

장점: 멀티 서버로 확장해도 동시성 제어 가능(“분산”의 핵심)  
단점: 락 만료시간(lease time), 장애, 네트워크 이슈 등 운영 고려사항 많음