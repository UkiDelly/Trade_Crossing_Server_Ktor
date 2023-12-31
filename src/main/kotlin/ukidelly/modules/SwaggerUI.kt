@file:OptIn(ExperimentalSerializationApi::class)

package ukidelly.modules

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.ktor.server.application.*
import kotlinx.serialization.ExperimentalSerializationApi

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
      url = "http://localhost:8080/api/v1"
      description = "개발 서버"
    }

    securityScheme("Access") {
      type = AuthType.HTTP
      scheme = AuthScheme.BEARER
      bearerFormat = "jwt"
    }

    securityScheme("Refresh") {
      type = AuthType.HTTP
      scheme = AuthScheme.BEARER
      bearerFormat = "jwt"
    }

  }
}
