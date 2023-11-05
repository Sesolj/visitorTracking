## 방문자 수 트래킹 API

### 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [이런 점을 고민했어요!](#이런-점을-고민했어요)
3. [이런 문제를 해결했어요!](#이런-문제를-해결했어요)
4. [이런 점을 보완하려고 해요!](#이런-점을-보완하려고-해요)
5. [API Docs](#api-docs)

<br/>

### 프로젝트 개요
---
방문자 수를 트래킹하는 간단한 서비스를 구현하였습니다.

#### API 목록
- url 정보를 저장하는 API
- 호출 시 url의 카운트를 증가하는 API
- 일간/누적 방문자 수를 조회하는 API
- 사용자가 설정한 기간의 방문자 수를 조회하는 API

#### 개발 환경 및 기술 스택
Java, Spring Boot, Gradle, JPA, MySQL, Swagger

#### ERD
![image](https://github.com/Sesolj/visitorTracking/assets/147023239/59999f25-db0a-4c56-8d14-ab541ed20324)

특정 날짜/기간의 방문자 수를 조회하기 위해 일자와 방문자 수를 저장하는 로그 테이블을 따로 생성하였습니다.

스케줄링 코드를 통해 매일 자정 각 url의 일자 별 방문자 수를 로그 테이블에 저장합니다.

최대 조회 가능 일자는 7일이며, 그 이상의 데이터는 최근 데이터로 UPDATE하도록 구현하였습니다.

<br/>

### 이런 점을 고민했어요!
---
#### Builder 패턴
처음 Java 학습 시에는 주로 Getter, Setter를 통해 데이터에 접근했습니다. 하지만 Entity Class에서 Setter를 사용하면 객체일관성을 해칠 수 있다는 것을 배우게 되었고, 이후 생성자를 통해 데이터에 값을 주입하고 있습니다. 해당 프로젝트 역시 객체일관성을 유지하고 객체 생성 시 명시적으로 필드를 선언함으로써 가독성을 높이는 Builder 패턴에 따라 코드를 작성하였습니다.

#### 날짜 관련 정보의 처리 방식 통일
코드의 일관성을 해치는 여러 요인 중 하나는 바로 날짜와 관련된 정보라고 생각합니다. 재직 당시 개발자에 따라 구현 방식이 달라, 데이터를 String으로 받아 형변환 하는 방법, 처음부터 DateTime 형으로 받는 방법이 코드에 혼재되어 있었기 때문입니다.

이를 계기로 날짜 관련 정보에 대한 처리 방식을 고려하게 되었고, 불필요한 형변환 과정을 줄이기 위해 String이 아닌 LocalDateTime 형으로 데이터를 받을 수 있게 했습니다. 이를 위해 DTO에 ```@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")``` 어노테이션을 추가하여 데이터를 직렬화하고, Swagger 사용자의 편의를 위해 ```@Schema``` 어노테이션으로 원하는 날짜 포맷을 hint로써 표기했습니다.

#### 테스트 격리
@Transactional이 내장되어 있는 @DataJpaTest 어노테이션을 통해 테스트용 데이터를 롤백하는 방식으로 테스트를 수행하였습니다.

개발 초기에는 @SpringBootTest를 사용했으나 ```@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)```와 같이 환경을 설정할 경우 별도의 Thread에서 스프링 컨테이너가 실행되어 롤백이 되지 않는 문제가 발생하였습니다.

따라서 이 경우에는 데이터를 전부 삭제시키는 방법으로 DB를 초기화해야 된다는 것을 알게 되었습니다. 이로 인해 별도의 테스트용 DB를 생성해야된다는 점, 또한 모든 Bean을 로드하는 @SpringBootTest의 특성이 가벼운 프로그램에는 맞지 않는다고 생각하여 생산성을 위해 테스트 격리 방식을 변경하였습니다.

#### @ControllerDevice와 커스텀 Enum을 통한 예외 처리
비즈니스 로직 예외 처리를 위해 개발자가 thorw할 수 있는 예외 처리 방식을 구현하였습니다.

예외 발생 시 반환할 Http Status와 메세지를 관리하기 위해 커스텀 Exception Enum과 RuntimeException을 상속받는 Custom Exception을 생성하고, @ControllerAdvice 어노테이션을 통해 다양한 예외를 일관적인 방식으로 처리할 수 있도록 코드를 작성하였습니다.
```
Exception 관리를 위한 별도의 패키지
├── exception
│   ├── ApiException
│   ├── ApiExceptionEntity
│   ├── ApiExceptionHandler
│   └── ExceptionEnum
├──
```

<br/>

### 이런 문제를 해결했어요!
---
#### 테스트 데이터가 롤백되지 않는 문제(Database 엔진 문제)
@DataJpaTest에 @Transactional이 내장되어 있음에도, 테스트 코드 실행 종료 후에 데이터가 롤백되지 않는 문제가 있었습니다. 특별한 Exception 발생 없이 로그상에는 RollBack이 되었다고 출력되어, 해결에 난항을 겪었습니다.

원인은 데이터베이스의 엔진이었습니다. 데이터베이스가 트랜잭션을 지원하지 않는 MyISAM 엔진으로 생성되어 있었기 때문이었습니다. ```ALTER TABLE {테이블명} ENGINE = INNODB``` 명령을 통해 엔진을 InnoDB로 변경하여 해결하였습니다. 이를 계기로 데이터베이스 엔진의 여러 종류, 그리고 트랜잭션 지원 여부와 같은 각 엔진의 특성을 학습할 수 있었습니다.

#### Bulk Update의 영속성 컨텍스트 문제
모든 레코드의 값을 한꺼번에 변경하는 Bulk update 기능 구현 후 테스트 코드에서 값 변경이 적용되지 않아 계속 테스트가 실패하는 문제가 있었습니다.

원인은 영속성 컨텍스트 때문이었습니다. 단일 update와 다르게 일괄 update 시에는 영속성 컨텍스트를 통해 Entity를 관리하지 못한다는 것을 알게 되었습니다. 따라서 별도로 ```EntityManager```를 주입받은 후 ```flush()```와 ```clear()```를 통해 데이터 반영 후 영속성 컨텍스트를 비우고 다시 조회하는 방법으로 문제를 해결하였습니다. 이를 계기로 JPA의 영속성 컨텍스트 개념과 특징에 대해 배우게 되었습니다.

#### @PathVariable 404 Request Error(Spring Security의 정규화로 인한 문제)
개발 초기 당시 Rest Api Naming/Design Convention에 유의하여 API를 설계하였습니다. 일환으로, 특정 한 건의 데이터를 조회하는 경우 URI에 파라미터를 표시하도록 Naming하고 컨트롤러는 @PathVariable을 통해 데이터를 전달받았습니다.

@RequestParam을 사용하는 것이 익숙했으나 해당 어노테이션은 특정 한 건의 데이터를 조회하기보다 정렬이 필요할 경우 사용하고, 자원의 '식별'이 필요할 경우에는 @PathVariable을 사용한다는 REST API의 가이드라인을 접했기 때문입니다.

하지만 개발 과정에서 @PathVariable로 데이터를 전달받자 404 Requst Error가 발생하였습니다. 원인은 /(slash)였습니다. Parameter로 URL을 받자, 슬래시가 resource 경로로 취급되었기 때문입니다. 이를 통해 Spring Security가 URL을 정규화하고 이중 슬래시를 단일 슬래시로 대체한다는 사실을 알게 되었습니다. 경로 매개 변수에 slash를 인코딩하는 방법이 있지만 잠재적인 Security 위험을 초래할 수 있어 권장되지 않기 때문에, @PathVariable을 쿼리스트링으로 동작하는 @RequestParam으로 변경하여 문제를 해결했습니다. 


<br/>

### 이런 점을 보완하려고 해요!
---
#### CI/CD 파이프라인 구축
Travis CI를 통해 CI를 구축하고자 했으나, 알 수 없는 이유로 Build 감지가 안되는 오류가 발생하였습니다. 

차선으로 CI tool을 Jenkins로 변경하였고, github와 연동하여 push 발생 시 ./gradlew clean test 명령어를 수행하도록 로컬 환경에 CI를 구축하였습니다.
이와 같이 현재는 빌드가 되면 github의 webhook 기능을 통해 Jenkins로 trigger를 유발하고 테스트 수행-빌드 과정이 이루어지게끔 되어있습니다.

더 나아가 클라우드 환경에서 지속적 통합/무중단 배포까지 할 수 있도록 CI/CD 파이프라인을 구축하고자 합니다.

<br/>

### API Docs
---
![image](https://github.com/Sesolj/visitorTracking/assets/147023239/5886c02f-82c7-4573-8919-ef544f3fe93a)


