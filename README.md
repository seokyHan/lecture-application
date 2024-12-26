# 요구사항 분석

- 먼저 크게 아래의 3가지 API 를 구현
    - 특강 신청 API
        - 특정 userId 로 선착순으로 제공되는 특강을 신청.
        - 동일한 신청자는 동일한 강의에 대해서 한번의 수강 신청만 성공할 수 있다.
        - 신청자가 이미 신청한 경우를 처리하기 위해, 신청 시점에 해당 유저의 신청 기록을 확인 필요.
        - 선착순 30명만 신청 가능.(즉 모든 특강은 정원이 30명)
        - 이미 30명의 정원이 모두 채워진 특강에 신청 요청시 실패. (예외 처리)
        - 특강 신청 기간(시간)이 아니거나, 마감 상태인 경우 실패(예외 처리)
    - 특강 신청 가능 목록 API
        - 날짜별로 현재 신청이 가능한 특강 목록이 조회되도록 함.
        - 조회 종료 일자가 조회 시작 일자보다 앞서있는 경우 요청 실패. (예외 처리)
    - 특강 신청 완료 목록 조회 API
        - 특정 userId 로 신청 완료된 특강 목록을 조회.
        - 각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 함.

- 세부 요구사항
    - 사용자 회원가입/로그인 기능은 구현 X.
    - 아키텍처 준수를 위한 애플리케이션 패키지 설계.
    - 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성.
        - 동시성 이슈를 고려 하여 구현하는데, 여러대의 동일한 애플리케이션 서버가 있다고 가정.
        - 서로 다른 애플리케이션 서버 간의 "동시성 이슈" 는 어떻게 해결해야 하는지를 RDBMS ( MySQL / MariaDB ) 기반으로 해결하도록 함.
    - DB 는 MySQL / MariaDB 로 제한.
    - Test 는 (1) 인메모리 DB (2) docker-compose 정도 허용. ( + TestContainers)
    - 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성.
    - 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증하는 **통합 테스트** 작성.
    - 동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공하는 것을 검증하는 **통합 테스트** 작성

- 기타 요구사항
- 설계한 테이블에 대한 **ERD** 및 이유를 설명하는 **README** 작성.

### 고민해야할 부분
- 정확하게 30 명의 사용자에게만 특강을 제공할 방법을 고민.
- 같은 사용자에게 여러 번의 특강 슬롯이 제공되지 않도록 제한할 방법을 고민.



#### 아키텍처와 패키지의 경우 Interfaces → Application → Domain → Infrastructure 흐름으로, 상위 레이어가 하위 레이어에 의존하지만 하위 레이어는 상위 레이어를 참조하지 않도록 설계 해봤습니다.


# API 명세서

### 특강 신청 API

- URL :  `/api/v1/lecture/enrollments`
- Method : POST
- Request Body

    ```jsx
    {
      "userId": 1,
      "lectureId": 1,
      "lectureScheduleId": 1
    }
    ```

- Response Body

    ```jsx
    {
      "lectureEnrollment": {
        "id": 1,
        "userId": 1,
        "lectureId": 1,
        "lectureScheduleId": 1,
        "createdAt": "2024-12-23T00:00:00",
        "updatedAt": null
      }
    }
    ```


### 특강 신청 가능 목록 API

- URL : `/api/v1/lecture/available`
- Method : GET
- Request Body

```jsx
{
  "date": "2024-10-03"
}
```

- Response Body

```jsx
{
  "lectureSchedules": [
    {
      "id": 1,
      "lectureId": 1,
      "date": "2024-12-23",
      "capacity": 30,
      "enrolledCount": 0,
      "status": "available",
      "startTime": "2024-12-23T00:00:00",
      "endTime": "2024-12-23T00:00:00",
      "createdAt": "2024-12-23T00:00:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "lectureId": 2,
      "date": "2024-12-23",
      "capacity": 30,
      "enrolledCount": 0,
      "status": "available",
      "startTime": "2024-12-23T00:00:00",
      "endTime": "2024-12-23T00:00:00",
      "createdAt": "2024-12-23T00:00:00",
      "updatedAt": null
    }
  ]
}
```

### 특강 신청 완료 목록 조회 API

- URL : `*/api/vi/lecture/enrolled*`
- Method : GET
- Request Body

```jsx
{
  "userId": 1
}
```

- Response Body

```jsx
{
  "lectureEnrollments": [
    {
      "id": 1,
      "title": "강의1",
      "description": "강의1에 대한 설명",
      "lecturer": {
        "id": 1,
        "name": "가나다1",
        "createdAt": "2024-12-23T00:00:00",
        "updatedAt": null
      },
      "createdAt": "2024-12-23T00:00:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "title": "강의2",
      "description": "강의2에 대한 설명",
      "lecturer": {
        "id": 2,
        "name": "가나다1",
        "createdAt": "2024-12-24T00:00:00",
        "updatedAt": null
      },
      "createdAt": "2024-12-24T00:00:00",
      "updatedAt": null
    }
  ]
}
```

# ERD 설계

<img width="703" alt="스크린샷 2024-12-27 오전 12 06 48" src="https://github.com/user-attachments/assets/ebd66feb-4a86-40ed-a365-6deef8d88430" />

# Users 테이블

사용자 정보 저장 테이블

- id (BIGINT): 사용자의 고유 식별자 (Primary Key, Auto Increment)
- name (VARCHAR(255)): 사용자 이름
- createdAt (DATETIME): 생성 시간
- updatedAt (DATETIME): 수정 시간

회원가입/로그인 기능은 미구현이므로 사용자 정보만 저장.

# **Lecture 테이블**

특강 정보를 저장하는 테이블

- id (BIGINT): 특강의 고유 식별자 (Primary Key, Auto Increment)
- title (VARCHAR(255)): 특강 제목
- description (TEXT): 특강 설명
- lecturerId (BIGINT): 강연자 ID (Users 테이블의 id를 참조)
- createdAt (DATETIME): 생성 시간
- updatedAt (DATETIME): 수정 시간

# **LectureSchedule 테이블**

특강 날짜 정보를 저장하는 테이블

- id (BIGINT): 특강 일정의 고유 식별자 (Primary Key, Auto Increment)
- lectureId (BIGINT): 특강 ID (Lecture 테이블의 id를 참조)
- capacity (INT): 특강 수용 인원
- enrolledCount (INT): 현재 등록된 인원 수
- status (VARCHAR(255)): 특강 상태
- startAt (DATETIME): 특강 시작 시간
- endAt (DATETIME): 특강 종료 시간
- createdAt (DATETIME): 생성 시간
- updatedAt (DATETIME): 수정 시간

특강은 날짜별로 여러개가 있을 수 있으므로 특강 날짜 정보를 저장한다.

# **LectureEnrollment 테이블**

특강 신청 정보를 저장하는 테이블

- id (BIGINT): 특강 등록의 고유 식별자 (Primary Key, Auto Increment)
- lectureId (BIGINT): 특강 ID (Lecture 테이블의 id를 참조)
- lectureScheduleId (BIGINT): 특강 일정 ID (LectureSchedule 테이블의 id를 참조)
- userId (BIGINT): 사용자 ID (User 테이블의 id를 참조)
- createdAt (DATETIME): 생성 시간
- updatedAt (DATETIME): 수정 시간

유니크 제약조건:
- (userId, lectureScheduleId): 한 사용자가 같은 특강 일정에 중복 등록할 수 없다.

특강 신청 API 구현 시 사용자가 동일 강의에 한 번만 신청 가능하도록 중복 신청을 방지하기 위해 사용.

# ERD 설계 이유

Users 테이블 분리:

- 사용자 정보를 별도 테이블로 관리해 향후 회원 기능 확장에 대비

Lecture와 LectureSchedule 분리:

- 하나의 특강이 여러 날짜에 걸쳐 진행될 수 있음을 고려
- 동일한 특강을 여러 시간대에 개설 가능

LectureSchedule의 capacity와 enrolledCount:

- 각 일정별 수용 인원과 현재 등록 인원 관리로 중복 등록 방지 및 남은 자리 계산이 용이함

LectureEnrollment 테이블:

- 사용자의 특강 신청 이력 관리 및 중복 신청 방지
- lectureId와 lectureScheduleId 저장으로 사용자의 특정 특강 및 일정 등록 정확히 추적
- userId와 lectureScheduleId의 조합에 복합 유니크 제약조건을 걸어 중복 등록 방지

확장성, 유지보수성, 데이터 무결성등 향후 기능확장을 고려해서 만들었으며, 유니크 제약조건을 걸어 데이터 무결성 보장, 중복 데이터 방지를 하도록 함

# 분산 환경에서의 동시성 제어
- 멀티스레드 환경 : 단일 인스턴스, 인스턴스 레벨 동시성 제어 가능
- 분산 환경 : 다중 인스턴스, 인스턴스 레벨 제어 불가능

### 분산 환경에서의 동시성 제어 방식
- #### 락(Lock)을 이용한 제어
  - 원리: 공유 자원에 대한 접근을 순차적으로 제어
  - 장점: 구현이 상대적으로 간단, 데이터 일관성 보장
  - 단점: 성능 저하 가능성, 데드락 위험

- #### 버전(Version)을 이용한 제어
  - 원리: 데이터 변경 시 버전 정보 업데이트
  - 장점: 충돌 감지 용이, 낙관적 동시성 제어에 적합
  - 단점: 추가적인 버전 관리 오버헤드

- #### 타임스탬프(Timestamp)를 이용한 제어
  - 원리: 각 트랜잭션에 고유한 타임스탬프 부여
  - 장점: 시간 기반 일관성 제공, 이력 추적 용이
  - 단점: 시계 동기화 문제, 높은 오버헤드 가능성
  
- #### 캐시(Cache)를 이용한 제어
  - 원리: 자주 사용되는 데이터를 로컬에 저장 및 관리
  - 장점: 빠른 데이터 접근, 네트워크 부하 감소
  - 단점: 캐시 일관성 유지의 어려움, 추가적인 동기화 메커니즘 필요

- #### 분산락(Distributed Lock)을 이용한 제어
  - 원리: 분산 환경에서 공유 자원에 대한 접근을 조정하기 위해 중앙화된 락 관리 시스템 사용
  - 장점: 분산 환경에서 강력한 일관성 보장 여러 노드 간 동기화 용이 세밀한 락 관리 가능 (예: 읽기/쓰기 락 구분)
  - 단점: 락 서버의 단일 장애점 가능성 네트워크 지연으로 인한 성능 저하 가능성 구현 및 운영의 복잡성
  - 구현 예시: Redis, ZooKeeper 등을 이용한 분산락 구현

### 비교 분석
- 성능 측면:
  - 캐시 > 버전 > 타임스탬프 > 분산락 > 락
- 구현 복잡도:
  - 락 < 버전 < 타임스탬프 < 캐시 < 분산락
- 일관성 보장:
  - 분산락 = 락 > 버전 > 타임스탬프 > 캐시
- 분산 환경 적합성:
  - 분산락 > 버전 > 캐시 > 타임스탬프 > 락
    
### 선택 기준
  - 시스템 요구사항 (성능, 일관성, 가용성)
  - 데이터 특성 (갱신 빈도, 크기)
  - 인프라 환경 (네트워크 지연, 노드 수)
  - 개발 및 유지보수 복잡도

### 결론
각 방식은 상황에 따라 장단점이 다르므로, 시스템의 특성과 요구사항을 고려해 적절한 접근을 선택해야 한다.
모든건 상황에 따라 다르며, Trade-off 또한 고려해야 한다.
이번 프로젝트의 경우 분산 환경에서의 동시성 제어를 고려해야 하므로, 분산락을 이용한 제어 방식을 적용하는 것이 적합할 것으로 판단됐다.
아직 배우지 않은 Redis, ZooKeeper등을 이용한 분산락 구현은 복잡도가 높고 어렵기 때문에, 락을 이용한 제어 방식을 우선 적용하고, 추후 분산락을 적용하는 것도 고려해볼 수 있다.
버전이나 타임스탬프를 이용한 방법은 데이터 변경이 빈번하게 일어나는(선착순, 티켓예매등) 경우에는 적합하지 않다고 해서 사용하지 않았다. 
캐시는 데이터 일관성 유지가 어렵고 추가적인 동기화 메커니즘이 필요하다고 해서 사용하지 않았다. 
그래서 단순한 DB레벨에서의 락을 이용한 방법을 사용하였다.