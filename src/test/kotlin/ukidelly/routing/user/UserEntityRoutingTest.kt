package ukidelly.routing.user

import io.ktor.client.request.*
import io.ktor.server.testing.*
import ukidelly.module
import kotlin.test.Test

class UserEntityRoutingTest {

    @Test
    fun testGet() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostLogin() = testApplication {
        application {
            module()
        }
        client.post("/login").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostRefresh() = testApplication {
        application {
            module()
        }
        client.post("/refresh").apply {
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