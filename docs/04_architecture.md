# 04. Architecture & Package

## Layer
- Controller: 요청/응답 DTO, validation, 인증 처리
- Service: 트랜잭션 경계, 도메인 규칙(선점/확정/취소)
- Repository: DB 접근
- Entity: JPA 엔티티
- DTO: API 요청/응답 모델

## Why this structure
- (이유를 3줄로: 책임 분리, 테스트 용이성, 트랜잭션 관리 명확)

## Transaction Strategy (Draft)
- hold 생성: @Transactional
- 결제 처리: @Transactional
- 만료 처리: 배치/스케줄러 or 수동 API

---

- Reservation/Payment 분리: 상태 머신과 책임 분리, 결제 확장 대비
- HOLD 필요: 외부 결제 시간을 트랜잭션 밖으로 빼면서 좌석 선점 보장
- 유니크/정합성: 서비스(락/분산락) + DB(최후 제약)
- 만료: 초기엔 수동 API로 검증 + 스케줄러로 자동화