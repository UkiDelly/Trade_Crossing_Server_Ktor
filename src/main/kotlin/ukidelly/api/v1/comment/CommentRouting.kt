package ukidelly.api.v1.comment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v1.comment.service.CommentService
import ukidelly.systems.models.ResponseDto

fun Route.commentRoutes() {

	val commentService by inject<CommentService>()

	// get all the comments
	get {
		val comments = commentService.getAllComment(1)
		call.respond(HttpStatusCode.OK, ResponseDto.Success(comments, "success"))
	}


	// do with a specific comment
	route("/{commentId}") {

		put { }

		delete { }

		post("/reply") { }
	}
}