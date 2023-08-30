# Trade Crossing Server

---

이 서버는 Trade Crossing 앱 프로젝트의 서버입니다.

이 서버는 `Kotlin`언어와 `Ktor` 프레인워크로 제작되었습니다.

이 레포의 파일구조는 다음과 같습니다.

```
📦ukidelly
┣ 📂api
┃ ┣ 📂v1
┃ ┃ ┣ 📂comment
┃ ┃ ┃ ┣ 📂models
┃ ┃ ┃ ┃ ┣ 📜Comment.kt
┃ ┃ ┃ ┃ ┗ 📜CommentDto.kt
┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┗ 📜CommentRepository.kt
┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┗ 📜CommentService.kt
┃ ┃ ┃ ┣ 📜.DS_Store
┃ ┃ ┃ ┗ 📜CommentRouting.kt
┃ ┃ ┣ 📂post
┃ ┃ ┃ ┣ 📂models
┃ ┃ ┃ ┃ ┣ 📂dto
┃ ┃ ┃ ┃ ┃ ┣ 📜LatestPostDto.kt
┃ ┃ ┃ ┃ ┃ ┗ 📜PostDetailDto.kt
┃ ┃ ┃ ┃ ┣ 📜Post.kt
┃ ┃ ┃ ┃ ┗ 📜PostCreateRequest.kt
┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┗ 📜PostRepository.kt
┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┗ 📜PostService.kt
┃ ┃ ┃ ┗ 📜PostRouting.kt
┃ ┃ ┣ 📂user
┃ ┃ ┃ ┣ 📂models
┃ ┃ ┃ ┃ ┣ 📜User.kt
┃ ┃ ┃ ┃ ┣ 📜UserLoginRequest.kt
┃ ┃ ┃ ┃ ┣ 📜UserRegisterRequest.kt
┃ ┃ ┃ ┃ ┗ 📜UserResponse.kt
┃ ┃ ┃ ┣ 📂repository
┃ ┃ ┃ ┃ ┗ 📜UserRepository.kt
┃ ┃ ┃ ┣ 📂service
┃ ┃ ┃ ┃ ┗ 📜UserService.kt
┃ ┃ ┃ ┗ 📜UserRouting.kt
┃ ┃ ┗ 📜.DS_Store
┃ ┗ 📜.DS_Store
┣ 📂database
┃ ┣ 📂entity
┃ ┃ ┣ 📜CommentEntity.kt
┃ ┃ ┣ 📜LikeEntity.kt
┃ ┃ ┣ 📜PostEntity.kt
┃ ┃ ┗ 📜UserEntity.kt
┃ ┣ 📂tables
┃ ┃ ┣ 📜CommentTable.kt
┃ ┃ ┣ 📜LikeTable.kt
┃ ┃ ┣ 📜PostTable.kt
┃ ┃ ┗ 📜UserTable.kt
┃ ┗ 📜DataBaseFactory.kt
┣ 📂modules
┃ ┣ 📜Koin.kt
┃ ┣ 📜Monitoring.kt
┃ ┣ 📜RequestValidation.kt
┃ ┣ 📜Routing.kt
┃ ┣ 📜Security.kt
┃ ┣ 📜Serialization.kt
┃ ┣ 📜StatusPage.kt
┃ ┗ 📜SwaggerUI.kt
┣ 📂systems
┃ ┣ 📂errors
┃ ┃ ┗ 📜CustomExcpetions.kt
┃ ┣ 📂models
┃ ┃ ┣ 📜Enums.kt
┃ ┃ ┣ 📜ResponseDto.kt
┃ ┃ ┗ 📜Token.kt
┃ ┗ 📜Config.kt
┣ 📂utils
┃ ┗ 📜Utils.kt
┣ 📜.DS_Store
┗ 📜Application.kt
```

`api` - 라우팅, 엔드포인트, 서비스, 각 기능의 레포지토리를 관리하는 폴더입니다.

`modules` - `Koin`, `Routing`등 프로젝트에 추가된 패키지들을 관리하기 위한 폴더입니다.

`systems` - 서버 설정, 커스텀 익셉션, 등등 서버 전역에 사용되는 모델, 파일을 관리하는 폴더입니다.

`utils` - 유틸리티들을 관리하는 폴더입니다.

`database` - DB 연결, Table, Entity들을 관리하는 폴더입니다.