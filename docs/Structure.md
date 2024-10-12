

## 패키지 구조 - Clean Architecture
```
src
├── main
│   ├── java
│   │   ├── com
│   │   │   └──example
│   │   │       ├── application       
│   │   │       ├── domain            
│   │   │       ├── infrastructure    
│   │   │       └── interfaces        
│   └── resources
└── test

```


## 기술스택
- 프로그래밍 언어: Java 17
- 도구: Gradle
- 프레임워크: Spring Boot
- ORM: JPA
- 데이터베이스:
  - H2 (테스트 및 개발 환경)
- API 문서화 도구:
  - SpringDoc OpenAPI 또는 Swagger
- 테스트 도구:
	-  JUnit
	- Mockito
	- AssertJ


