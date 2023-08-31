//package ukidelly.api.v1.post
//
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.server.testing.*
//import org.junit.Assert.assertEquals
//import org.slf4j.LoggerFactory
//import kotlin.test.Test
//
//class PostRoutingTest {
//
//    private val logger = LoggerFactory.getLogger("PostRoutingTest")
//
//    @Test
//    fun testGetPostid() = testApplication {
//        client.get("post/1").apply {
//            assertEquals(HttpStatusCode.OK, this.status)
//            logger.debug("post : {}", this.bodyAsText())
//        }
//    }
//}