package ukidelly.modules

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.*

fun Application.configureSwaggerUI() {
  //routing {
  //    swaggerUI(path = "swagger") {
  //    }
  //}

  install(SwaggerUI) {
    swagger {
      swaggerUrl = "swagger"
      forwardRoot = true
    }

    info {
      title = "Trade Crossing API"
      version = "1.0.0"
      description = "Trade Crossing 프로젝트의 API 문서입니다."
    }

    server {
      url = "http://localhost:8080"
      description = "개발 서버"
    }
  }
}
