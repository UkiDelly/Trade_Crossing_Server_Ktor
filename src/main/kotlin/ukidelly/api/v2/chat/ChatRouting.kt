package ukidelly.api.v2.chat

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Route.chatRouting() {

    authenticate("auth-jwt") {
        post("/new") {

        }
    }

    //    val client = HttpClient {
    //        install(ContentNegotiation) {
    //            json(
    //                Json {
    //                    prettyPrint = true
    //                    isLenient = true
    //                    ignoreUnknownKeys = true
    //                    namingStrategy = SnakeCase
    //                    serializersModule = SerializersModule {
    //                        contextual(UUID::class, UUIDSerializerModule)
    //                        contextual(LocalDateTime::class, LocalDateTimeSerializerModule)
    //                    }
    //                }
    //            )
    //        }
    //    }.apply {
    //        headers {
    //            append(HttpHeaders.ContentType, ContentType.Application.Json)
    //        }
    //    }


    //    authenticate("auth-jwt") {
    //        post("/new") {
    //            runBlocking {
    //                client.post {
    //                    url("http://localhost:8080/api/v1/chat")
    //
    //                }
    //
    //            }
    //        }
    //
    //        post("/join") { }
    //
    //        delete("/{chatId}") { }
    //    }


}