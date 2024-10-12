
### 1. 유저 대기열 토큰 발급 및 검증
- N초마다 K명씩 토큰을 비활성 -> 활성으로 전환
- 콘서트마다 큐 엔트리 생성

```mermaid
sequenceDiagram
  participant User
  participant Queue
  participant Token
  participant Database
  participant Scheduler

  User ->> Queue: 대기열 진입 요청
  Queue ->> Database: 대기열 상태 조회 (유저 등록 확인)
  alt 유저가 대기열에 없음
    Queue ->> Token: 대기열 토큰 발급 요청 (대기 상태)
    Token ->> Database: 토큰 발급 정보 저장
    Token ->> User: 토큰 발급 완료 (비활성 상태)
  else 유저가 이미 대기열에 있음
    Queue ->> User: 대기 중 알림
  end

  loop N초마다
    User ->> Queue: 대기 상태 확인 요청 (토큰 포함)
    Queue ->> Database: 대기 상태 조회 (폴링 방식)
    Queue ->> User: 대기 상태 정보 반환 (대기 순번, 남은 시간)

    Scheduler ->> Queue: N분마다 K명 활성화 요청
    Queue ->> Database: 비활성 유저 K명 조회 (비활성 상태)
    alt 유저의 대기 순번 도래
      Queue ->> Token: K명 유저의 토큰 활성화 요청 (활성 상태 전환)
      Token ->> Database: 토큰 상태 업데이트 (활성 상태로 변경)
      Token ->> Queue: 활성화 완료 알림
      Queue ->> User: 활성 상태로 전환 알림
    else 아직 대기 중
      Queue ->> User: 대기 상태 유지 알림 (대기 순번, 남은 시간)
    end
  end
```

### 2. 콘서트의 예약가능 날짜 조회
```mermaid
sequenceDiagram
    participant User
    participant Token
    participant Concert
    participant Schedule
    participant Database

    User ->> Token: 대기열 통과 토큰 전송
    Token ->> Database: 토큰 정보 조회 및 검증
    Token ->> User: 토큰 검증 완료

    User ->> Concert: 예약 가능한 스케줄 조회 요청 
    Concert ->> Schedule: 해당 콘서트의 스케줄 조회 요청
    Schedule ->> Database: 스케줄 데이터 조회
    alt 스케줄 있음
        Schedule ->> User: 스케줄 목록 반환
    else 스케줄 없음
        Schedule ->>  User: "예약 가능한 스케줄이 없습니다." 오류 반환
    end
```
### 3. 선택한 날짜의 좌석 조회

```mermaid
sequenceDiagram
    participant User
    participant Token
    participant Schedule
    participant Seat
    participant Database

    User ->> Token: 대기열 통과 토큰 전송
    Token ->> Database: 토큰 정보 조회 및 검증
    Token ->> User: 토큰 검증 완료

    User ->> Schedule: 특정 스케줄 좌석 조회 요청
    Schedule ->> Seat: 해당 스케줄의 좌석 상태 조회 요청
    Seat ->> Database: 좌석 데이터 조회
    
    alt 예약 가능한 좌석 있음
        Seat ->> User: 예약 가능한 좌석 목록 반환
    else 예약 가능한 좌석 없음
        Seat ->> User: "예약 가능한 좌석이 없습니다." 오류 반환
    end
```

### 4. 좌석 예약 요청 API
```mermaid
sequenceDiagram
    participant User
    participant Token
    participant Seat
    participant Reservation
    participant Database

    User ->> Token: 대기열 통과 토큰 전송
    Token ->> Database: 토큰 정보 조회 및 검증
    Token ->> User: 토큰 검증 완료

    User ->> Seat: 좌석 예약 요청 (날짜, 좌석 정보)
    Seat ->> Database: 좌석에 대한 락 획득 시도
    alt 락 획득 성공
        Database -->> Seat: 락 획득 성공
        Seat ->> Reservation: 임시 좌석 예약 처리 요청 (5분 임시 배정)
        Reservation ->> Database: 트랜잭션 시작
        Database -->> Reservation: 트랜잭션 시작됨
        Reservation ->> Database: 좌석 예약 상태 업데이트
        Database -->> Reservation: 상태 변경 성공
        Reservation ->> Database: 트랜잭션 커밋
        Database -->> Reservation: 트랜잭션 커밋 성공
        Seat ->> Database: 좌석에 대한 락 해제
        Database -->> Seat: 락 해제됨
        Seat ->> User: 예약 성공 (5분간 임시 배정 상태)

    else 락 획득 실패
        Database -->> Seat: 락 획득 실패
        Seat ->> User: 이미 선택된 좌석입니다.
    end
```

### 5. 잔액 조회 
- 잔액조회는 대기열을 거처야만 할 수 있는것이 아니기 떄문에 토큰검증을 하지않음
```mermaid
sequenceDiagram
    participant User
    participant Balance
    participant Database

    User ->> Balance: 사용자 잔액 조회 요청 (사용자 식별자)
    Balance ->> Database: 사용자 식별자를 통해 잔액 정보 조회
    alt 잔액 정보 존재
        Database -->> Balance: 잔액 정보 반환
        Balance -->> User: 잔액 정보 반환
    else 잔액 정보 없음
        Database -->> Balance: "잔액 정보 없음" 오류 반환
        Balance -->> User: "잔액 정보가 없습니다." 오류 반환
    end
```

### 6. 잔액 충전
- 잔액충전은 대기열을 거쳐야만 할 수 있는것이 아니기 떄문에 토큰 검증을 하지않음
```mermaid
sequenceDiagram
    participant User
    participant Balance
    participant Payment
    participant Database

    User ->> Balance: 잔액 충전 요청 (사용자 식별자, 충전 금액)
    Balance ->> Database: 트랜잭션 시작
    Database -->> Balance: 트랜잭션 시작됨

    Balance ->> Database: 잔액 업데이트 요청 (충전 금액 추가)
    alt 잔액 업데이트 성공
        Database -->> Balance: 잔액 업데이트 성공
        Balance ->> Payment: 충전 내역 기록 요청 (사용자 식별자, 충전 금액)
        Payment ->> Database: 충전 내역 저장
        alt 충전 내역 저장 성공
            Database -->> Payment: 충전 내역 저장 성공
            Balance ->> Database: 트랜잭션 커밋 요청
            Database -->> Balance: 트랜잭션 커밋 성공
            Balance ->> User: 충전 완료 메시지 및 새로운 잔액 반환
        else 충전 내역 저장 실패 (네트워크 오류 등)
            Payment ->> Balance: 충전 내역 저장 실패
            Balance ->> Database: 트랜잭션 롤백 요청
            Database -->> Balance: 트랜잭션 롤백 성공
            Balance ->> User: "충전 중 오류가 발생했습니다. 다시 시도해주세요." 오류 반환
        end
    else 잔액 업데이트 실패 (네트워크 오류 등)
        Balance ->> Database: 트랜잭션 롤백 요청
        Database -->> Balance: 트랜잭션 롤백 성공
        Balance ->> User: "충전 중 오류가 발생했습니다. 다시 시도해주세요." 오류 반환
    end

```

### 7. 결제 API
- 결제 요청을 처리하기 전에 대기열 검증을 통과해야 하고, 결제가 성공하면 토큰을 만료시킴.
```mermaid
sequenceDiagram

    participant User
    participant Token
    participant Payment
    participant Seat
    participant Reservation
    participant Database

    User ->> Token: 대기열 통과 토큰 전송
    Token ->> Database: 토큰 정보 조회 및 검증
    Token ->> User: 토큰 검증 완료

    User ->> Payment: 결제 요청 (좌석 정보, 결제 금액)
    Payment ->> Seat: 좌석 상태 확인 요청
    Seat ->> Reservation: 좌석 상태 확인
    Reservation ->> Database: 좌석 상태 조회
    Database -->> Reservation: 좌석 상태 정보 반환

    alt 임시 배정 시간이 만료되기 전에 결제
        Payment ->> Database: 트랜잭션 시작
        Payment ->> Database: 사용자 잔액 확인
        Database -->> Payment: 잔액 반환
        alt 잔액 부족
            Payment -->> User: 결제 실패 (잔액 부족)
        else 잔액 충분
            Payment ->> Database: 결제 내역 생성
            Payment ->> Seat: 좌석 소유권 배정 요청
            Seat ->> Reservation: 좌석 상태 업데이트 (예약 완료)
            Reservation ->> Database: 좌석 상태 "완료" 업데이트
            Database -->> Reservation: 상태 변경 성공
            Reservation ->> Payment: 좌석 소유권 배정 완료
            Payment ->> Token: 대기열 토큰 만료 요청
            Payment ->> Database: 트랜잭션 커밋
            Database -->> Payment: 트랜잭션 커밋 완료
            Payment -->> User: 결제 성공 및 좌석 소유권 배정 완료
        end
    else 임시 배정 시간이 만료됨
        Seat ->> Database: 좌석 상태 만료 트랜잭션 시작
        Seat ->> Reservation: 좌석 상태 업데이트 (만료)
        Reservation ->> Database: 좌석 상태 "만료" 업데이트
        Database -->> Reservation: 상태 변경 성공
        Seat ->> Database: 좌석 상태 업데이트 트랜잭션 커밋
        Database -->> Seat: 트랜잭션 커밋 완료

        Payment ->> Database: 트랜잭션 롤백
        Database -->> Payment: 트랜잭션 롤백 완료
        Payment -->> User: "임시 배정 시간이 만료되었습니다." 결제 실패 반환
    end
```