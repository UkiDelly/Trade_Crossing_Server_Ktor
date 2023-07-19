package ukidelly.plugins

import io.ktor.server.plugins.callloging.*
import org.slf4j.event.*
import io.ktor.server.request.*
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
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
