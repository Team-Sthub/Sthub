## ✏️ Sthub : 학업을 위한 중고장터
- 4학년 1학기 소프트웨어시스템 개발

|팀장|팀원|팀원|팀원|
|:------:|:---:|:------:|:---:|
|정채원|김지민|손효정|유승연|

<br>

## 1️⃣ 서비스 소개
- **Sthub** : 한층 더 유용한 학업을 위한 장터
- 학업 관련 상품을 중고거래 및 공동구매할 수 있는 서비스
- 기존 중고 장터는 학업 용품에 중점을 두지 않아 이를 보완한 서비스

<br>

## 2️⃣ 핵심 기능
**✅ 중고거래**
- 판매하고 싶은 상품의 카테고리, 상품명, 설명, 사진, 가격, 거래 방식 등을 상세하게 작성하여 업로드
- 실시간 1:1 쪽지 및 댓글을 통해 상품에 대한 문의와 계약 진행
- 구매 및 결제
- 배송 조회, 거래 후기 작성

**✅ 공동구매**
- 공동구매하고 싶은 상품, 기한, 나눔 장소, 오픈채팅 카톡 링크, 설명 등을 상세하게 작성하여 업로드
- 실시간 1:1 쪽지 및 댓글을 통해 상품에 대한 문의와 계약 진행
- 공동구매 참여 및 신청자 목록 조회

**✅ 검색**
- 내근처 : 현재 위치를 기반으로 근처에서 진행 중인 중고거래, 공동구매 게시글 추천
- 검색 : 카테고리별 검색, 키워드 검색

<br>

## 3️⃣ 테스트용 ID/PW
### ⚠️ 주의사항
- application.yml에서 active profile이 prod로 설정되어 있는지 확인
- 중고거래 타유저가 구매 -> 작성자가 거래 최종 방식 선택 필수
- 배송 조회 및 후기 작성은 마이페이지 내역에서 더보기 페이지에서 가능
- 공구 신청 수락되어야만 참여자의 마이페이지 공구참여란에 해당 게시글 조회 가능
  
### 🔐 계정
1. 계정 1
   - ID : cspark
   - PWD : cspark1111!
2. 계정 2
   - ID : gywjd
   - PWD : gywjd1111!
3. 계정 3
   - ID : mangba
   - PWD : MANGBA1111!

<br>

## 4️⃣ Commit Message Convention

> 커밋태그(#이슈 번호): 내용

ex. `feat(#40) : login 파일 추가'

| command | mean |
| --- | --- |
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 수정 |
| style | 코드 포맷 변경 |
| test | 테스트 코드 추가 |
| refactoring | 코드 리팩토링 |
| chore | 빌드, 패키지 매니저 수정 |

<br>

## 5️⃣ Development Environment
<p align="left">
<img src ="https://img.shields.io/badge/Thymeleaf-5.0-005F0F">
<img src ="https://img.shields.io/badge/SpringBoot-13.3-6DB33F">
<img src ="https://img.shields.io/badge/SpringDataJPA-15.0-6DB33F">
<img src ="https://img.shields.io/badge/Java-17.0-white">
