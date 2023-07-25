package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import ukidelly.api.v1.user.UserLoginRequest
import ukidelly.api.v1.user.UserRegisterRequest

fun Application.configureRequestValidation() {
    install(RequestValidation) {


        // 로그인의 요청 파라미터를 검증합니다.
        validate<UserLoginRequest> {

            if (it.snsId == "") {
                ValidationResult.Invalid("sns_id가 비어있습니다.")
            } else if (it.email.isEmpty()) {
                ValidationResult.Invalid("email이 비어있습니다.")
            } else {
                ValidationResult.Valid
            }

        }


        // 회원가입 요청
        validate<UserRegisterRequest> {

            if (it.snsId == "" || it.snsId.isEmpty()) {
                ValidationResult.Invalid("sns_id가 비어있습니다.")
            }
            if (!it.validEmail()) {
                ValidationResult.Invalid("잘못된 email 형식입니다.")
            } else {
                ValidationResult.Valid
            }
        }
    }
}