# API 명세

## 🔑 대기열 토큰 생성 요청 API
- URL: /api/v1/queue/tokens
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
  "uuid": "user-uuid",
  "concertId": 1,
  "tokenStatus": "INACTIVE",
  "createdAt": "2024-10-10T12:00:03",
}
```

## 🔑  대기열 상태 확인 API
- URL: /api/v1/queue/tokens/{tokenId}/status
- 메서드: GET
- 설명: 유저는 자신의 대기열 상태를 확인한다.
- Status Code: 200 OK
- ResponseBody(대기 중):
```
{
  "tokenId": 1,
  "uuid": "user-uuid",
  "status": "WAITING",
  "remainingQueueCount": 5
}
```
- ResponseBody(대기 완료):
```
{
  "tokenId": 1,
  "uuid": "user-uuid",
  "status": "COMPLETED",
  "remainingQueueCount": 0
}
```

## 🔑 토큰 활성화 API
- URL: /api/v1/queue/tokens/{tokenId}/activate
- 메서드: PATCH
- 설명: 대기가 완료되면 비활성 토큰을 활성토큰으로 전환한다.
- Request Body:
```
{
  "tokenStatus": "ACTIVE"
}
```
- Status Code: 200 OK
- ResponseBody:
```
{
  "tokenId": 1,
  "uuid": "user-uuid",
  "tokenStatus": "ACTIVE",
  "createdAt": "2024-10-10T12:00:05",
  "activatedAt": "2024-10-10T12:00:10"
```

## 🔑 토큰 검증 API
- URL: /api/v1/queue/tokens/{tokenId}
- 메서드: GET
- 설명: 활성된 토큰인지 검사한다.
- Status Code: 200 OK
- ResponseBody(유효한 토큰):
```
{
  "isValid": true,
  "tokenId": 1,
  "uuid":"user-uuid"
}
```
- ResponseBody(유효하지 않은 토큰):
```
{
  "isValid": false,
  "tokenId": 1,
  "uuid":"user-uuid"
}
```


---
## 🎤 콘서트 일정 조회 API
- URL: /api/v1/concerts/{concertId}/schedules
- 메서드: GET
- 설명: 콘서트의 예약 가능한 날짜를 조회한다.
- Status Code: 200 OK
- ResponseBody:
```
{
  "concertId": 1,
  "concertTitle": "콘서트 제목",
  "concertDescription": "콘서트 설명",
  "openDate": "2024-10-01T10:00:00",
  "schedules": [
    {
      "date": "2024-10-15",
      "availableSeats": 30
    },
    {
      "date": "2024-10-16",
      "availableSeats": 40
    }
  ]
}
```

## 🎤 예약가능 좌석 조회 API
- URL: /api/v1/concerts/{concertId}/schedules/{scheduleId}/seats
- 메서드: GET
- 설명: 선택한 콘서트 일정의 예약 가능한 좌석을 조회한다.
- Status Code: 200 OK
- ResponseBody:
```
{
  "concertId": 1,
  "scheduleId": 1,
  "availableSeats": [1, 2, 3, 4, 5]
}
```

## 🎤 좌석 예약 요청 API
- URL: /api/v1/concerts/{concertId}/schedules/{scheduleId}/reservation
- 메서드: POST
- 설명: 사용자가 좌석 예약을 요청한다.
- Request Body:
```
{
  "tokenId": 1,
  "seatNumber": 13
}
```
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "tokenId":1,
  "concertId": 1,
  "scheduleId": 2,
  "seatNumber": 13,
  "seatStatus": "TEMPORARY"
  "reservationStatus":"TEMPORARY"
  "reservedAt": "2024-10-15 12:00:05"
}
```

## 💳 잔액조회 API
- URL: /api/v1/payment/balance/{userId}
- 메서드: GET
- 설명: 사용자가 잔액을 조회한다.
- Status Code: 200 OK
- ResponseBody:
```
{
  "userId": 1,
  "balance": 50000,
  "updatedAt": "2024-10-10T12:00:03"
}
```

## 💳 잔액충전 API
- URL:  /api/v1/payment/balance/charge
- 메서드: POST
- 설명: 사용자가 금액을 충전한다.
- Request Body:
```
{
  "userId": 1,
  "amount": 100000
}
```
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "userId":1,
  "chargedAmount": 100000,
  "chargedAt": "2024-10-10T12:00:03",
  "updatedAmount": 150000
}
```

## 💳 결제 API
- URL: /api/v1/payment/reservation/{reservationId}/payment
- 메서드: POST
- 설명:  예매한 콘서트 좌석을 결제한다.
- Request Body:
```
{
  "tokenId":1,
  "amount": 100000
}
```
- Status Code: 201 Created
- ResponseBody:
```
{ 
  "tokenId":1,
  "reservationId":1,
  "concertId": 1,
  "scheduleId": 2,
  "seatNumber": 13,
  "seatStatus": "CONFIRMED",
  "reservationStatus":"CONFIRMED",
  "reservedAt": "2024-10-15 12:00:05",
  "updatedAt":  "2024-10-15 12:00:10",
  "amount":110000
}
```