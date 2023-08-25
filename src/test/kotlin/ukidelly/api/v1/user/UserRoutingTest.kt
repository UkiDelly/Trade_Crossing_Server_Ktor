package ukidelly.api.v1.user

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import ukidelly.api.v1.user.models.UserLoginRequest
import ukidelly.module
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRoutingTest {


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
		application {
			module()
		}
		client.post("/register").apply {
			TODO("Please write your test here")
		}
	}
}


