package ukidelly.api.v1.feed


import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.feed.service.FeedService

fun Route.feedRouting() {

    val logger = LoggerFactory.getLogger("FeedRouting")
    val feedService by inject<FeedService>()

    // 최신 자유게시판 가져오기
    get<FeedRoutes.Latest> { param ->

    }

    // {feedId}의 게시글 가져오기
    get<FeedRoutes.FeedId> { }

    authenticate("auth-jwt") {

        // 새로운 게시글 생성하기
        post<FeedRoutes.New> { }

        // 게시글 수정하기
        put<FeedRoutes.FeedId> { }

        // 게시글 삭제하기
        delete<FeedRoutes.FeedId> { }


    }


    //get("/latest") {
    //    val queryParam = call.request.queryParameters
    //    val missingParam = mutableMapOf<String, String>()
    //
    //    if (!queryParam.contains("page")) {
    //        missingParam["page"] = "page를 입력해주세요."
    //    } else if (queryParam["page"]!!.toInt() <= 0) {
    //        missingParam["page"] = "page는 0보다 큰 정수를 입력해주세요."
    //    }
    //
    //    if (!queryParam.contains("size")) {
    //        missingParam["size"] = "size를 입력해주세요."
    //    } else if (queryParam["size"]!!.toInt() <= 0) {
    //        missingParam["size"] = "size는 0보다 큰 정수를 입력해주세요."
    //    }
    //
    //
    //
    //    if (missingParam.isNotEmpty()) {
    //        call.respond(
    //            HttpStatusCode.BadRequest,
    //            ResponseDto.Error(ServerError.InvalidField, "실패")
    //        )
    //        return@get
    //    } else {
    //        val posts = feedService.getLatestPosts(
    //            page = queryParam["page"]!!.toInt(),
    //            size = queryParam["size"]!!.toInt()
    //        )
    //        call.respond(HttpStatusCode.OK, ResponseDto.Success("", "성공"))
    //    }
    //
    //
    //}
    //
    //get("/{postId}") { }
    //
    //authenticate("auth-jwt") {
    //
    //    post("/new") {
    //        val userId = call.principal<UserIdPrincipal>()!!
    //        val body = call.receive<NewFeedDto>()
    //        feedService.addNewFeed(userId.name.toInt(), body)
    //        call.respond(HttpStatusCode.OK, ResponseDto.Success("", "성공"))
    //
    //    }
    //
    //    route("/{postId}") {
    //
    //        put { }
    //
    //        delete { }
    //    }
    //}
}



