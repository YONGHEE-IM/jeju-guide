# Jeju Guide (개인 프로젝트)

> 제주 여행 정보 + 커뮤니티(게시판) + 일정 추천(AI 플래너) 기능을 제공하는 웹 서비스  
> **Spring Boot + Thymeleaf + Spring Security + JPA + MySQL**

---

## 1) 프로젝트 소개
- 관광지/맛집 정보를 목록/상세로 제공하고  
- 회원가입/로그인 후 게시판과 댓글로 후기를 공유하며  
- 입력 조건을 바탕으로 **여행 일정 추천(규칙 기반 플래너)**을 제공하는 서비스입니다.

---

## 2) 주요 기능

### 여행 정보
- 관광지 목록/상세
- 맛집 목록/상세

### 회원 기능
- 회원가입 / 로그인 / 로그아웃 (Spring Security)
- 마이페이지
- 계정 찾기 / 비밀번호 재설정 흐름

### 커뮤니티
- 게시글 CRUD + 페이징
- 댓글 작성/수정/삭제

### AI 일정 플래너
- 화면: `/ai/plan`
- API: `POST /api/ai/plan`
- 현재는 **규칙 기반 더미 구현**이며, 추후 LLM 연동으로 확장 가능하도록 구조를 분리했습니다.

---

## 3) 기술 스택
- **Backend**: Java 17, Spring Boot, Spring MVC, Spring Security
- **Template**: Thymeleaf
- **DB**: MySQL, Spring Data JPA(Hibernate)
- **Build/ETC**: Gradle, Validation(Bean Validation)

---

## 4) 프로젝트 구조(예시)
- `controller/` : 회원/게시판/AI 플래너/관리 페이지
- `service/` : 비즈니스 로직
- `domain/` : Member, BoardPost/Comment 등 엔티티
- `src/main/resources/templates` : Thymeleaf 화면

---

## 5) 실행 방법(로컬)

### 1. MySQL DB 생성
```sql
CREATE DATABASE jeju_guide DEFAULT CHARACTER SET utf8mb4;

2. 설정 파일 수정
src/main/resources/application.properties에서 DB 계정 정보 수정

3. 실행
./gradlew bootRun

4. 접속
http://localhost:7080

