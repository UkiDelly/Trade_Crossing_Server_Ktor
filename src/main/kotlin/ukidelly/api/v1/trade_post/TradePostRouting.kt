package ukidelly.api.v1.trade_post

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import ukidelly.api.v1.trade_post.comment.tradePostCommentRoutes
import ukidelly.api.v1.trade_post.models.TradePostCreateRequest
import ukidelly.api.v1.trade_post.service.TradePostService
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.post.TradePostEntity
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.ResponseDto

fun Route.tradePostRouting() {
    val tradePostService by inject<TradePostService>()

    // 최신 게시글 가져오기
    get("/latest") {

        val missingParam = mutableMapOf<String, String>()
        val queryParam: Parameters = call.request.queryParameters

        if (!queryParam.contains("page")) {
            missingParam["page"] = "page를 입력해주세요."
        } else if (queryParam["page"]!!.toInt() <= 0) {
            missingParam["page"] = "page는 0보다 큰 정수를 입력해주세요."
        }

        if (!queryParam.contains("size")) {
            missingParam["size"] = "size를 입력해주세요."
        } else if (queryParam["size"]!!.toInt() <= 0) {
            missingParam["size"] = "size는 0보다 큰 정수를 입력해주세요."
        }

        if (missingParam.isNotEmpty()) {
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseDto.Error(ServerError.InvalidQueryParameter, missingParam.values.toString())
            )
            return@get
        } else {
            val posts = tradePostService.getLatestPosts(
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
                tradePostService.getPost(postId.toInt())?.let {
                    call.respond(HttpStatusCode.OK, ResponseDto.Success(it, "성공"))
                } ?: run {
                    call.respond(HttpStatusCode.NotFound, ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시물입니다"))
                }
            } ?: run {
                call.respond(HttpStatusCode.BadRequest, ResponseDto.Error(ServerError.InvalidField, "postId를 입력해주세요"))
            }
        }

        // 게시물 수정 삭제 (토큰 필요)
        authenticate("auth-jwt") {
            // 수정
            put { }

            // 삭제
            delete {
                val userId = call.principal<UserIdPrincipal>()!!.name
                call.parameters["postId"]?.let {
                    val post =
                        withContext(Dispatchers.IO) { dbQuery { database -> TradePostEntity.findById(it.toInt()) } }

                    if (post == null) {
                        call.respond(
                            HttpStatusCode.NotFound,
                            call.respond(
                                HttpStatusCode.NotFound,
                                ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시물입니다")
                            )
                        )
                        return@delete
                    } else if (post.userId != userId.toInt()) {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            ResponseDto.Error(ServerError.UnAuhtorized, "본인만 이 게시물을 삭제할 수 있습니다.")
                        )
                        return@delete
                    } else {
                        val status = tradePostService.deletePost(postId = it.toInt())
                        call.respond(HttpStatusCode.OK, ResponseDto.Success("삭제 성공", "성공"))
                    }
                }

            }


        }

        // 댓글ㅣ
        route("/comment") {
            tradePostCommentRoutes()
        }
    }

    authenticate("auth-jwt") {
        post("/new") {

            val principal = call.principal<UserIdPrincipal>()!!
            val userId = principal.name.toInt()
            val tradePostCreateRequest = call.receive<TradePostCreateRequest>()
            tradePostService.addNewPost(tradePostCreateRequest, userId)?.let {
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