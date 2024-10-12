# API 명세

## 🔑 대기열 토큰 생성 요청 API
- URL: /api/v1/tokens
- 메서드: POST
- 설명: 대기열 진입 후 토큰을 생성한다.
- Request Body:
```
{
  "uuid": "user-uuid",
  "concertId": 1
}
```
- Status Code: 201 Created
- ResponseBody:
```
{
  "tokenId": 1,
  "concertId": 1,
  "tokenStatus": "INACTIVE",
  "queueNo": 1
}
```

## 🔑  대기열 상태 확인 API
- URL: /api/v1/tokens/{tokenId}
- 메서드: GET
- 설명: 유저는 자신의 대기열 상태를 확인한다.
- Status Code: 200 OK
- ResponseBody(대기 중):
```
{
  "tokenId": 1,
  "status": "WAITING",
  "remainingQueueCount": 5
}
```
- ResponseBody(대기 완료):
```
{
  "tokenId": 1,
  "status": "COMPLETED",
  "remainingQueueCount": 0
}
```

## 🔑 토큰 활성화 API
- URL: /api/v1/tokens/{tokenId}
- 메서드: PATCH
- 설명: 대기가 완료되면 비활성 토큰을 활성토큰으로 전환한다.
- Status Code: 200 OK
- ResponseBody:
```
{
  "tokenId": 1,
  "tokenStatus": "ACTIVE"
  "expiredAt": "2024-10-10T12:00:10"
}
```

## 🔑 토큰 검증 API
- URL: /api/v1/tokens/{tokenId}
- 메서드: HEAD
- 설명: 활성된 토큰인지 검사한다.
- Status Code: 200 OK
- ResponseBody(유효한 토큰):
```
HTTP/1.1 200 OK
```
- ResponseBody(유효하지 않은 토큰):
```
HTTP/1.1 404 Not Found
```


---
## 🎤 콘서트 일정 조회 API
- URL: /api/v1/concerts/{concertId}/schedules
- 메서드: GET
- 설명: 콘서트의 예약 가능한 날짜를 조회한다.

- **이 API를 사용하기 전에, 토큰 검증 API를 통해 토큰의 유효성을 확인해야 함.**
- 검증 API: **`/api/v1/tokens/{tokenId}`**
- Request Headers: `Authorization: Bearer <tokenId>`
- Status Code: 200 OK
- ResponseBody:
```
{
  "concertId": 1,
  "concertTitle": "콘서트 제목",
  "concertDescription": "콘서트 설명",
  "startDate": "2024-10-09",
  "endDate": "2024-10-10",
  "schedules": [
    {
      "date": "2024-10-15",
      "availableSeats": 30,
      "totalSeats": 50
    },
    {
      "date": "2024-10-16",
      "availableSeats": 40,
      "totalSeats": 50
    }
  ]
}
```

## 🎤 예약가능 좌석 조회 API
- URL: /api/v1/concerts/{concertId}/schedules/{scheduleId}/seats
- 메서드: GET
- 설명: 선택한 콘서트 일정의 예약 가능한 좌석을 조회한다.
- 검증 API: **`/api/v1/tokens/{tokenId}`**
- Request Headers: `Authorization: Bearer <tokenId>`
- Status Code: 200 OK
- ResponseBody:
```
{
  "concertId": 1,
  "scheduleId": 1,
  "availableSeats": [1, 2, 3, 4, 5],
  "price":80000,
  "status":"AVAILABLE"
}
```

## 🎤 좌석 예약 요청 API
- URL: /api/v1/concerts/{concertId}/schedules/{scheduleId}/reservations
- 메서드: POST
- 설명: 사용자가 좌석 예약을 요청한다.
- 검증 API: **`/api/v1/tokens/{tokenId}`**
- Request Headers: `Authorization: Bearer <tokenId>`
- Request Body:
```
{
  "seatNumber": 13
}
```
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "concertId": 1,
  "scheduleId": 2,
  "seatNumber": 13,
  "seatStatus": "TEMPORARY"
  "reservationStatus":"TEMPORARY"
  "reservedAt": "2024-10-15 12:00:05"
  "expiredAt": "2024-10-15 12:00:05"
}
```

## 💳 잔액조회 API
- URL: /api/v1/users/{userId}/wallets
- 메서드: GET
- 설명: 사용자가 잔액을 조회한다.
- Status Code: 200 OK
- ResponseBody:
```
{
  "amount": 50000,
  "updatedAt": "2024-10-10T12:00:03"
}
```

## 💳 잔액충전 API
- URL:  /api/v1/users/{userId}/wallets
- 메서드: POST
- 설명: 사용자가 금액을 충전한다.
- Request Body:
```
{
  "amount": 100000
}
```
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "chargedAmount": 100000,
  "chargedAt": "2024-10-10T12:00:03",
  "updatedAmount": 150000
}
```

## 💳 결제 API
- URL: /api/v1/reservations/{reservationId}/payments
- 메서드: POST
- 설명:  예매한 콘서트 좌석을 결제한다.
- 검증 API: **`/api/v1/tokens/{tokenId}`**
- Request Headers: `Authorization: Bearer <tokenId>`
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "reservationId":1,
  "concertId": 1,
  "scheduleId": 2,
  "seatNumber": 13,
  "seatStatus": "CONFIRMED",
  "reservationStatus":"CONFIRMED",
  "reservedAt": "2024-10-15 12:00:05",
  "updatedAt":  "2024-10-15 12:03:05",
  "expiredAt":  "2024-10-15 12:05:05",
  "amount":110000
}
```