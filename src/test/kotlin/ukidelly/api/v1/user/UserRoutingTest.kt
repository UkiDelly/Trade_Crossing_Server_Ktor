//package ukidelly.api.v1.user
//
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.server.testing.*
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.JsonNamingStrategy.Builtins.SnakeCase
//import kotlinx.serialization.json.buildJsonObject
//import kotlinx.serialization.json.put
//import org.slf4j.LoggerFactory
//import ukidelly.api.v1.user.models.EmailLoginReqeust
//import ukidelly.api.v1.user.models.SocialLoginRequest
//import ukidelly.systems.models.DefaultProfile
//import ukidelly.systems.models.LoginType
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class UserRoutingTest {
//
//    val logger = LoggerFactory.getLogger("UserRoutingTest")
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    @Test
//    fun testEmailLogin() = testApplication {
//        client.post("/user/login/email") {
//            headers {
//                append(HttpHeaders.ContentType, ContentType.Application.Json)
//            }
//
//
//            val json = Json {
//                namingStrategy = SnakeCase
//            }
//            setBody(
//                json.encodeToString(EmailLoginReqeust("test@test.com", "test"))
//            )
//
//
//        }.apply {
//            assertEquals(HttpStatusCode.OK, this.status)
//        }
//    }
//
//    @OptIn(ExperimentalSerializationApi::class)
//    @Test
//    fun testPostLoginGoogle() = testApplication {
//        application {
////			module()
//        }
//
//        client.post("/user/login/google") {
//            headers {
//                append(HttpHeaders.ContentType, ContentType.Application.Json)
//            }
//            setBody(
//                SocialLoginRequest(
//                    "102802897338938679561",
//                    "ukidelly@gmail.com"
//                ).toJsonString()
//
//            )
//
//
//        }.apply {
//            println(this.bodyAsText())
//            assertEquals(this.status, HttpStatusCode.OK)
//        }
//    }
//
//
//    @Test
//    fun testPostRegister() = testApplication {
//        client.post("user/register") {
//            headers {
//                append(HttpHeaders.ContentType, ContentType.Application.Json)
//            }
//
//            setBody(Json.encodeToString(buildJsonObject {
//                put("sns_id", "")
//                put("login_type", LoginType.email.name)
//                put("profile", DefaultProfile.nook.name)
//                put("email", "test@test.com")
//                put("password", "test")
//                put("user_name", "Email Test")
//                put("island_name", "Email Test Island")
//                put("introduction", "Email Test Introduction")
//            }))
//        }.apply {
//
//            assertEquals(HttpStatusCode.OK, this.status)
//            logger.debug("response: {}", this.bodyAsText())
//        }
//    }
//}
//
//
