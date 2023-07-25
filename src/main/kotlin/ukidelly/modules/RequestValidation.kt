package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import ukidelly.api.v1.user.UserLoginRequest

fun Application.configureRequestValidation() {
    install(RequestValidation) {


        // 로그인의 요청 파라미터를 검증합니다.
        validate<UserLoginRequest> {

            if (it.snsId.isEmpty()) {

                ValidationResult.Invalid(" sns_id가 비어있습니다. ")
            } else if (it.email.isEmpty()) {

                ValidationResult.Invalid(" email이 비어있습니다. ")
            }

            ValidationResult.Valid
        }
    }
}