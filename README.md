# Trade Crossing Server

---

> 기획 문서: https://daehyeonsong.notion.site/Trade-Crossing-f30972e680ed4fbebe0340b956237e0c?pvs=4

이 서버는 Trade Crossing 프로젝트의 메인 서버이며, `Kotlin`언어와 `Ktor` 프레임워크로 제작되었습니다.

## 특징

- Jetbrain에서 내놓은 `Koltin` 경량 웹프레임워크인 `Ktor`를 사용함으로써 빠른 구동과, 코루틴을 사용하여 DB 입출력을 비동기적으로 작업을 수행할 수 있습니다.
- (개인적인 견해) 객체지향으로 설계되어 있지만 커스텀 쿼리 처리가 매우 불편한 `JPA` 대신 코틀린에서만 사용이 가능한 `Exposed` ORM 라이브러리를 사용함으로써, `JPA`와 비슷한 `DAO`
  방식과 `QueryDSL`와 비슷한 `DSL` 방식으로 DB 접근이 가능합니다.
- Di 라이브러리로 `Koin`을 사용했고, 각 API의 Repository, Service을 필요한 곳에서 사용할수 있게 주입하는 방식을 사용했습니다.
- Spring (Boot)에서 사용하는 MVC 패턴을 사용하고 있습니다.

## API 목록

- /user
    - 로그인, 회원가입할때 사용되는 엔드 포인트
    - 로그인은 email, 소셜 로그인으로 분리
    - 이메일 로그인 / 회원가입의 경우 비밀번호를 암호화하여 DB에 저장

- /trade-post
    - 거래 피드에 사용되는 엔드 포인트
    - 피드 리스트 및, 게시물 조회는 인증없이 가능하
    - 생성/수정/삭제는 인증된 사용자만 가능

- /trade-post/{trade-postId}/comment
    - 거래 게시물의 댓글 엔드포인트
    - 인증된 사용자만 사용 가능

- /image
    - 이미지를 업로드/다운로드하는 엔드포인트
    - 업로드된 이미지는 Supabase의 Bucket에 객체로 저장
- /feed
    - 홈피드 엔드포인트
    - 조회에는 인증이 필요하지 않음
    - 생성/수정/삭제는 인증된 사용자만 사용 가능