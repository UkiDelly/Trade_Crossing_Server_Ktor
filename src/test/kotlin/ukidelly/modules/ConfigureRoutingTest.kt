package ukidelly.modules

import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.user.service.UserService
import kotlin.test.Test

class ConfigureRoutingTest {


    val userService by inject<UserService>(UserService::class.java)

    @Test
    fun testPatchPost() = testApplication {
        application {
            configureRouting()
        }
        client.patch("/post").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetPostPostid() = testApplication {
        application {
            configureRouting()
        }
        client.get("/post/{postId}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeletePostDelete() = testApplication {
        application {
            configureRouting()
        }
        client.delete("/post/delete").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetPostLatest() = testApplication {
        application {
            configureRouting()
        }
        client.get("/post/latest").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostPostNew() = testApplication {
        application {
            configureRouting()
        }
        client.post("/post/new").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetUserLogin() = testApplication {
        application {
            configureRouting()
        }
        client.get("/user/login").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetUserRefresh() = testApplication {
        application {
            configureRouting()
        }
        client.get("/user/refresh").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostUserRegister() = testApplication {
        application {
            configureRouting()
        }
        client.post("/user/register").apply {
            TODO("Please write your test here")
        }
    }
}