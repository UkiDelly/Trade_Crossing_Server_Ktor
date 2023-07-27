package ukidelly.api.v1.post

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.koin.ktor.ext.inject
import ukidelly.api.v1.post.models.PostDto
import ukidelly.api.v1.post.service.PostService
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.post.PostTable
import java.util.*

fun Route.postRouting() {


    val postService by inject<PostService>()


    get("/latest") {}


    get("/{postId}") {

        call.parameters["postId"]?.let { postId ->

        } ?: run {

        }
    }

    authenticate("auth-jwt") {


        post("/new") {


            dbQuery {
                SchemaUtils.create(PostTable)
            }

            val principal = call.principal<UserIdPrincipal>()!!
            val userId = UUID.fromString(principal.name)
            val post = call.receive<PostDto>()
            postService.addNewPost(post, userId)

            call.respond(HttpStatusCode.OK, "등록 성공")


        }


        patch { }


        delete("/delete") {}


//        post("/like") {}
//
//
//        post("/unlike") {}
//
//
//        post("/comment") {}
//
//
//        post("/editComment") {}
//
//
//        post("/deleteComment") {}
//
//
//        post("/likeComment") {}
//
//
//        post("/unlikeComment") {}
    }
}