package ukidelly.api.v1.post

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v1.comment.commentRoutes
import ukidelly.api.v1.post.models.PostCreateRequest
import ukidelly.api.v1.post.service.PostService
import ukidelly.systems.models.ResponseDto
import java.util.*

fun Route.postRouting() {
	val postService by inject<PostService>()

	// 최신 게시글 가져오기
	get("/latest") {

		val missingParam = mutableMapOf<String, String>()
		val queryParam: Parameters = call.request.queryParameters

		if (!queryParam.contains("page")) {
			missingParam["page"] = "page를 입력해주세요"
		} else if (queryParam["page"]!!.toInt() <= 0) {
			missingParam["page"] = "page는 0보다 큰 정수를 입력해주세요"
		}

		if (!queryParam.contains("size")) {
			missingParam["size"] = "size를 입력해주세요"
		} else if (queryParam["size"]!!.toInt() <= 0) {
			missingParam["size"] = "size는 0보다 큰 정수를 입력해주세요"
		}

		if (missingParam.isNotEmpty()) {
			call.respond(HttpStatusCode.BadRequest, ResponseDto.Error(missingParam, "파라미터가 잘못되었습니다."))
			return@get
		} else {
			val posts = postService.getLatestPosts(
				itemsPerPage = queryParam["size"]!!.toInt(),
				page = queryParam["page"]!!.toInt()
			)
			call.respond(HttpStatusCode.OK, ResponseDto.Success(posts, "성공"))
		}

	}

	// 특정 게시물
	route("/{postId}") {

		// 게시물 가져오기
		get {

			call.parameters["postId"]?.let { postId ->
				postService.getPost(postId.toInt())?.let {
					call.respond(HttpStatusCode.OK, ResponseDto.Success(it, "성공"))
				} ?: run {
					call.respond(HttpStatusCode.NotFound, ResponseDto.Error("존재하지 않는 게시물입니다", "다시 시도해주세요"))
				}
			} ?: run {
				call.respond(HttpStatusCode.BadRequest, ResponseDto.Error("postId를 입력해주세요", "게시물을 가져오는데 실패하였습니다."))
			}
		}

		// 게시물 수정 삭제 (토큰 필요)
		authenticate("auth-jwt") {
			// 수정
			put { }

			// 삭제
			delete { }


		}

		// 댓글ㅣ
		route("/comment") {
			commentRoutes()
		}
	}



	authenticate("auth-jwt") {
		post("/new") {

			val principal = call.principal<UserIdPrincipal>()!!
			val userId = UUID.fromString(principal.name)
			val postCreateRequest = call.receive<PostCreateRequest>()
			postService.addNewPost(postCreateRequest, userId)?.let {
				call.respond(HttpStatusCode.OK, ResponseDto.Success(it, "성공"))
			} ?: run {
				call.respond(HttpStatusCode.BadRequest, ResponseDto.Error("게시물을 등록하지 못했습니다.", "실패"))
			}

		}


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