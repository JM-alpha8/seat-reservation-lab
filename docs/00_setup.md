# 00. Local Setup

## Goal
Spring Boot + MySQL + Redis를 로컬에서 동일한 방식으로 재현 가능한 환경으로 만든다.

## Stack
- Spring Boot 4.0.3, Java 17
- MySQL 8.0 (Docker)
- Redis 7 (Docker)

## Run DB/Redis
```bash
docker compose -f docker/docker-compose.local.yml up -d
docker ps
```

## Run App
- IntelliJ에서 실행 (profile: local)

## Health Check
```bash
http://localhost:8080/actuator/health


http://localhost:8080/health

(시큐리티가 기본적으로 막아서 임시 로그인 페이지로 이동)
아이디: user
패스워드: 아래와 같이 실행 로그에 있음.
Using generated security password: 87f2400d-e85d-4a6f-b9f5-0b88c991b36a
```


## Notes
- mysql 로컬 실행 포트 3307 
- netstat -ano | findstr :3306 (누가 사용하는지 확인) => 6064 사용중
- tasklist /FI "PID eq 6064"  (로컬 mysql이 사용중)