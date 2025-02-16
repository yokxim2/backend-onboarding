# 🛡️ Spring Boot JWT 인증/인가 시스템

Spring Security와 JWT를 활용한 인증/인가 시스템을 구축하였으며,  
회원가입 및 로그인 기능을 구현하고 AWS EC2에 배포 완료했습니다.  
Swagger UI를 통해 API 테스트가 가능합니다.

## 🚀 배포 URL
- **메인 경로:** [http://13.209.73.36:8080/](http://13.209.73.36:8080/)
- **Swagger UI:** [http://13.209.73.36:8080/swagger-ui/index.html](http://13.209.73.36:8080/swagger-ui/index.html)

## 🔑 주요 기능
✅ **회원가입 및 로그인 (JWT 기반 인증/인가)**  
✅ **Access Token & Refresh Token 발급 및 검증**  
✅ **Spring Security 적용**  
✅ **Swagger UI를 통한 API 테스트 제공**  
✅ **Postman을 사용한 API 테스트 완료**  
✅ **AWS EC2에 배포 완료**  

---

## 🛠️ API 명세

### 🔹 **회원가입**
- **URL:** `POST /api/user/signup`
- **요청 예시 (JSON)**
  ```json
  {
    "username": "testuser",
    "password": "securepassword",
    "email": "test@example.com"
  }
  
- **응답 예시 (JSON)**
  ```json
  {
  "message": "회원가입이 완료되었습니다."
  }

### 🔹 **로그인**
- **URL:** `POST /api/user/signup`
- **요청 예시 (JSON)**
  ```json
  {
  "username": "testuser",
  "password": "securepassword"
  }
  
- **응답 예시 (JSON)**
  ```json
  {
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
  }

## Swagger UI
- Swagger를 통해 API를 테스트할 수 있습니다.
- Swagger UI: http://13.209.73.36:8080/swagger-ui/index.html

## 실행 방법 (로컬 환경)

### 프로젝트 클론
```
git clone <REPO_URL>
cd backend-onboarding
```

### 빌드 및 실행
```
./gradlew clean build
java -jar build/libs/backend-onboarding.jar
```


## 배포 환경
- 플랫폼 : AWS EC2 (Ubuntu)
- 웹 프레입워크 : Spring Boot
- 보안 : Spring Security, JWT 인증
- DB : MySQL

