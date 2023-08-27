package ukidelly.api.v1.user

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.slf4j.LoggerFactory
import ukidelly.api.v1.user.models.UserLoginRequest
import ukidelly.module
import ukidelly.systems.models.DefaultProfile
import ukidelly.systems.models.LoginType
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRoutingTest {

	val logger = LoggerFactory.getLogger("UserRoutingTest")


	@Test
	fun testPostLoginApple() = testApplication {
		application {
			module()
		}
		client.post("/login/apple").apply {
			TODO("Please write your test here")
		}
	}

	@Test
	fun testPostLoginAuto() = testApplication {
		application {
			module()
		}
		client.post("/login/auto").apply {
			TODO("Please write your test here")
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	@Test
	fun testPostLoginGoogle() = testApplication {
		application {
//			module()
		}

		client.post("/user/login/google") {
			headers {
				append(HttpHeaders.ContentType, ContentType.Application.Json)
			}
			setBody(
				UserLoginRequest(
					"102802897338938679561",
					"ukidelly@gmail.com"
				).toJsonString()

			)


		}.apply {
			println(this.bodyAsText())
			assertEquals(this.status, HttpStatusCode.OK)
		}
	}

	@Test
	fun testPostLoginKakao() = testApplication {
		application {
			module()
		}


		client.post("/login/kakao").apply {
			TODO("Please write your test here")
		}
	}

	@Test
	fun testPostLoginRefresh() = testApplication {
		application {
			module()
		}
		client.post("/login/refresh").apply {
			TODO("Please write your test here")
		}
	}

	@Test
	fun testPostRegister() = testApplication {
//		application {
//			module()
//		}
		client.post("user/register") {
			headers {
				append(HttpHeaders.ContentType, ContentType.Application.Json)
			}

			setBody(Json.encodeToString(buildJsonObject {
				put("sns_id", "")
				put("login_type", LoginType.email.name)
				put("profile", DefaultProfile.nook.name)
				put("email", "test@test.com")
				put("user_name", "Email Test")
				put("island_name", "Email Test Island")
				put("introduction", "Email Test Introduction")
			}))
		}.apply {

			assertEquals(HttpStatusCode.OK, this.status)
			logger.debug("response: {}", this.bodyAsText())
		}
	}
}


