# MyPet Life

## 1. 🐶🐱 프로젝트 소개 
### 프로젝트 링크
http://13.209.174.161:8080

### 프로젝트 기간
22.08.09 ~ 22.09.14

### 프로젝트 설명
반려 동물 관리 서비스 마이 펫 라이프(MyPet Life)
마이 펫 라이프는 반려 동물을 키우는데 필요한 다양한 서비스를 제공합니다. 마이 펫 라이프를 통해 일정 관리, 사용자 간 커뮤니티 활동, 병원 정보 조회, 채팅 서비스 등을 경험하실 수 있습니다. 

----
## 2. 👩👩👩🧑 팀원 소개 및 담당 역할
### 팀장 [이지선](https://github.com/jsl1113)
- ERD 설계 및 구축
- 반려동물 일정 관리 기능 CRUD 구현
- Naver Cloud Platform 의 SMS Reservation API 를 이용한 일정 알림 설정 기능
- MockMvc 를 통해 일정 등록, 삭제 기능 단위 테스트 코드 작성
- 로그인/회원 가입, 일정 관리 프론트 구현
    - JavaScript의 fetch() 를 통한 양방향 JSON 통신

### 팀원 [이소희](https://github.com/olsohee)
- 로그인/회원가입 기능 구현
- OAuth2를 이용한 카카오 로그인 기능 CRUD 구현
- JWT 토큰 방식의 로그인 기능 구현
- 커뮤니티 기능 구현
- 프로젝트 전반적인 예외 처리 
- AWS 서버 구축, GitHub Actions와 AWS CodeDeploy를 이용한 CI/CD 환경 구축

### 팀원 [최수민](https://github.com/csumin0825)
- 로그인/회원가입 기능 구현
- JWT 토큰 방식의 로그인 기능 구현
- WebSocket을 이용한 채팅 문의 기능 구현
- 로그아웃, 커뮤니티 프론트 구현
  - JavaScript의 fetch()를 통한 양방향 JSON 통신

### 팀원 [신재원](https://github.com/shinjaewon99)
- ERD 설계 및 구축
- 공공 데이터 포탈의 오픈 API 사용하여 동물병원 데이터를 통한 동물병원 조회 기능 구현
- 동물병원 리뷰 기능 CRUD 구현
- 스웨거 설정
---

## 3. 🎮 핵심 기능
![mypetlife 프로젝트 설계도 drawio](https://github.com/my-pet-life/Final_Project_20Team/assets/108605017/d4d34b29-1a82-40d0-875b-9e6bc12811c1)
### 회원가입 및 로그인
- OAuth 2.0을 이용한 카카오 로그인 기능
- JWT 방식의 로그인 기능
  - access token 만료 후 refresh token을 이용해 access token 재발급 가능

### 일정 관리
- 병원 방문일, 예방 주사 접종일자 등 반려 동물 일정들을 기록 하기 위한 일정 관리 CRUD 기능
- 알림 설정 시, 해당 일정 전에 메세지 알림 전송받는 기능
  - Naver SMS Reservation API 이용 

### 커뮤니티
- 게시물 CRUD 기능
- 게시판별, 태그별, 반려동물 종별 게시글 조회 기능
- 게시글 좋아요, 댓글 좋아요 기능

### 병원 정보 조회 및 리뷰 작성
- 서울특별시, 경기도의 시 군 구 지역을 필터링 하여 해당 지역에 해당하는 동물병원을 조회 기능
- 네이버 지도 API를 사용하여 동물병원 주소 검색 기능
- 정상 영업중인 지역별 동물병원에 대한 리뷰 CRUD 기능
- 
### 채팅
- 실시간 채팅으로 관리자에게 궁금한 내용 문의 가능
- webSocket을 실시간 채팅 기능 구현
- 한 채팅방에 채팅방을 만든 유저와 관리자만 입장 가능

---

## 4. 💻 기술 스택
![image](https://github.com/my-pet-life/Final_Project_20Team/assets/55522275/6a852510-8d45-487f-bdc8-622ddd026bdd)


---

## 5. 💾 DB 다이어그램
![MyPet Life](https://github.com/my-pet-life/Final_Project_20Team/assets/108605017/fd06b745-b616-4873-b197-8b2f5835ae6d)

---

## 6. 📡 API 명세서
https://northern-dodo-61b.notion.site/63907edbbea14b3bb4d7f95540548969?v=0d60d451f3454bbc8fbc10f80f46e3f1&pvs=4

---

## 7. 🛠️ CI/CD
![image](https://github.com/my-pet-life/Final_Project_20Team/assets/108605017/711ceba4-1218-4fdf-a2db-1fba55ccc68d)
- GitHub Actions와 AWS S3, CodeDeploy를 이용하여 CI/CD 환경을 구축했습니다.
- main 브랜치에 push하면 GitHub Actions가 동작하고 빌드 파일이 S3에 저장됩니다. 
- CodeDeploy는 S3에 저장된 파일을 EC2 서버에 올리고 빌드 파일을 실행하여 배포합니다. 

