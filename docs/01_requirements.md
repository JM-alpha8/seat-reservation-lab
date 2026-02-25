# 01. Requirements

## Service Goal
- 영화관 예약 시스템

## Core Rules (정합성)
- 동일 Slot 내 동일 Seat는 동시에 1명만 예약 확정 가능
- 선점(HOLD)은 일정 시간 후 자동 해제됨
- 결제 성공 시 예약 확정(CONFIRMED)
- 결제 실패/취소 시 선점 해제(CANCELLED)

## Use Cases (8)
1. 회원 가입
2. 로그인(JWT)
3. 특정 Venue의 Slot 목록 조회
4. 특정 Slot의 좌석 현황 조회
5. 좌석 선점(HOLD) 생성
6. 결제 시도(성공/실패 시뮬레이션) -> 예약 확정/취소
7. 내 예약 목록 조회
8. 선점 만료 처리(스케줄러 or 이벤트)

## Non-Functional
- 동시 요청 100건 상황에서 중복 예약이 발생하지 않아야 함
- 로그로 문제 재현/해결 과정을 추적 가능해야 함