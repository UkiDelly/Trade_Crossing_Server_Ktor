//package ukidelly.api.v1.user
//
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.server.config.*
//import io.ktor.server.testing.*
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonNamingStrategy
//import kotlinx.serialization.modules.SerializersModule
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import ukidelly.dto.requests.SocialLoginRequestDto
//import ukidelly.modules.LocalDateTimeSerializerModule
//import ukidelly.modules.UUIDSerializerModule
//import ukidelly.systems.models.LoginType
//import java.time.LocalDateTime
//import java.util.*
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class UserRoutingTest {
//
//    @BeforeEach
//    fun setup() {
//
//    }
//
//    @AfterEach
//    fun teardown() {
//    }
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    private val jsonConvertor = Json {
//        prettyPrint = true
//        isLenient = true
//        ignoreUnknownKeys = true
//        namingStrategy = JsonNamingStrategy.SnakeCase
//        serializersModule = SerializersModule {
//            contextual(UUID::class, UUIDSerializerModule)
//            contextual(LocalDateTime::class, LocalDateTimeSerializerModule)
//        }
//    }
//
//    @Test
//    fun testPostUserLoginSocial() = testApplication {
//
//        environment {
//            config = ApplicationConfig("test-application.yaml")
//        }
//        application {
//        }
//
//        val res = client.post("/user/login/social") {
//            contentType(ContentType.Application.Json)
//            setBody(
//                jsonConvertor.encodeToString(
//                    SocialLoginRequestDto(
//                        snsId = "1",
//                        email = "test@apple.com",
//                        loginType = LoginType.apple,
//                    )
//                )
//            )
//        }
//        assertEquals(res.status, HttpStatusCode.OK)
//    }
//
//    @Test
//    fun testPingPong() = testApplication {
//        environment {
//            config = ApplicationConfig("test-application.yaml")
//        }
//        application {
//            //module()
//        }
//
//        client.get("/").apply {
//            println(this.bodyAsText())
//        }
//    }
//
//}