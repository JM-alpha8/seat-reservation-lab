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