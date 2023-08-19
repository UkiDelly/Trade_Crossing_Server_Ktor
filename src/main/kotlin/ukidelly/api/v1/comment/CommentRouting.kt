package ukidelly.api.v1.comment

import io.ktor.server.routing.*

fun Route.commentRoutes() {

	// get all the comments
	get {

	}

	// do with a specific comment
	route("/{commentId}") {

		put { }

		delete { }

		post("/reply") { }
	}
}