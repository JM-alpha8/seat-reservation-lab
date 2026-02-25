# 02. ERD (Draft)

## User
- id (PK)
- email (UK)
- password_hash
- created_at

## Venue
- id (PK)
- name

## Slot
- id (PK)
- venue_id (FK -> Venue)
- start_at
- end_at
- status (OPEN/CLOSED)

## Seat
- id (PK)
- venue_id (FK -> Venue)
- seat_code (예: A1, A2)

## Reservation
- id (PK)
- user_id (FK -> User)
- slot_id (FK -> Slot)
- seat_id (FK -> Seat)
- status (HOLD/CONFIRMED/CANCELLED/EXPIRED)
- hold_expires_at
- created_at

## Payment
- id (PK)
- reservation_id (FK -> Reservation, UNIQUE)
- status (INIT/SUCCESS/FAILED)
- amount
- created_at

## Constraints
- (slot_id, seat_id) 조합은 "CONFIRMED" 상태에서 중복 불가
- Payment는 Reservation 당 1개

## Notes
- Reservation/Payment 분리: 상태 머신과 책임 분리, 결제 확장 대비
- HOLD 필요: 외부 결제 시간을 트랜잭션 밖으로 빼면서 좌석 선점 보장
- 유니크/정합성: 서비스(락/분산락) + DB(최후 제약)
- 만료: 초기엔 수동 API로 검증 + 스케줄러로 자동화