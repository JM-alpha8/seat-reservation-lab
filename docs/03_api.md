# 03. API Draft

## Auth
- POST /api/auth/signup
- POST /api/auth/login

## Browse
- GET /api/venues
- GET /api/venues/{venueId}/slots
- GET /api/slots/{slotId}/seats

## Reservation
- POST /api/reservations/hold   (slotId, seatId)
- POST /api/reservations/{reservationId}/pay (success=true/false)
- GET /api/reservations/me

## Admin/Job
- POST /api/admin/reservations/expire (테스트용 수동 실행)