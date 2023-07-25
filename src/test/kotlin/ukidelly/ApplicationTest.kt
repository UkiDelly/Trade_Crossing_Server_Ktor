package ukidelly

import io.ktor.client.request.*
import io.ktor.server.testing.*
import ukidelly.modules.configureRouting
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/login").apply {

        }
    }
}
