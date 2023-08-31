package ukidelly.api.v1.comment

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.slf4j.LoggerFactory
import ukidelly.module
import kotlin.test.Test
import kotlin.test.assertEquals

class TradePostCommentRoutesTest {

    val logger = LoggerFactory.getLogger("CommentRoutesTest")


    @Test
    fun testGet() = testApplication {

        client.get("/post/1/comment").apply {
            logger.debug("comments : {}", this.bodyAsText())
            assertEquals(HttpStatusCode.OK, this.status)
        }
    }

    @Test
    fun testDeleteCommentid() = testApplication {
        application {
            module()
        }
        client.delete("/{commentId}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutCommentid() = testApplication {
        application {
            module()
        }
        client.put("/{commentId}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostCommentidReply() = testApplication {
        application {
            module()
        }
        client.post("/{commentId}/reply").apply {
            TODO("Please write your test here")
        }
    }
}