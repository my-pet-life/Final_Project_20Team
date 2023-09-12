# MyPet Life

## 1. 🐶🐱 프로젝트 소개 
### 프로젝트 링크
(배포 링크)

### 프로젝트 기간
22.08.09 ~ 22.09.14

### 프로젝트 설명
반려 동물 관리 서비스 마이 펫 라이프(MyPet Life)
마이 펫 라이프는 반려 동물을 키우는데 필요한 다양한 서비스를 제공합니다. 마이 펫 라이프를 통해 일정 관리, 사용자 간 커뮤니티 활동, 병원 정보 조회, 채팅 서비스 등을 경험하실 수 있습니다. 

----
## 2. 👩👩👩🧑 팀원 소개 및 담당 역할
### 팀장 [이지선](https://github.com/jsl1113)

### 팀원 [이소희](https://github.com/olsohee)
- 로그인/회원가입 기능, 커뮤니티 기능 개발
- OAuth2를 이용한 카카오 로그인 구현
- JWT 토큰 발급
- 프로젝트 전반적인 예외 처리 
- AWS 서버 배포와 GitHub Actions와 AWS CodeDeploy를 이용한 CI/CD 환경 구축

### 팀원 [최수민](https://github.com/csumin0825)

### 팀원 [신재원](https://github.com/shinjaewon99)

---

## 3. 🎮 핵심 기능
(프론트 사진, 개발 과정이나 핵심 내용 작성하기)
### 회원가입 및 로그인
- OAuth 2.0을 이용한 카카오 로그인 구현
- JWT 방식의 로그인 구현

### 일정 관리
### 커뮤니티
- 
### 병원 정보 조회 및 리뷰 작성
### 채팅

---

## 4. 💻 기술 스택

---

## 5. 💾 DB 다이어그램

---

## 6. 📡 API 명세서

---

## 7. 🛠️ CI/CD
![image](https://github.com/my-pet-life/Final_Project_20Team/assets/108605017/711ceba4-1218-4fdf-a2db-1fba55ccc68d)
- GitHub Actions와 AWS S3, CodeDeploy를 이용하여 CI/CD 환경을 구축했습니다.
- main 브랜치에 push하면 GitHub Actions가 동작하고 빌드 파일이 S3에 저장됩니다. 
- CodeDeploy는 S3에 저장된 파일을 EC2 서버에 올리고 빌드 파일을 실행하여 배포합니다. 

---

## 8. 🏠 프로젝트 구조

