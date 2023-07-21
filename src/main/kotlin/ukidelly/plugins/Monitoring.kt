package ukidelly.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/api/v1") }
//        format { call ->
//
//            val requestPath = call.request.path()
//            val method = call.request.httpMethod.value.uppercase()
//            val requestLog = "$method $requestPath"
//
//
//
//
//            "$requestLog -> $responseLog"
//        }

    }
}
