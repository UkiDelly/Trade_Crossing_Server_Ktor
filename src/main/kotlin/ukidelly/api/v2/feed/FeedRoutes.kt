package ukidelly.api.v2.feed

import io.ktor.resources.*


@Resource("/feeds")
class FeedRoutes(val page: Int = 1, val size: Int = 10) {

    @Resource("{feedId}")
    class FeedId(val feedRoute: FeedRoutes = FeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val feed: FeedId)


    }
}


@Resource("comments")
class FeedCommentRoutes(val feed: FeedRoutes.FeedId) {

    @Resource("{commentId}")
    class CommentId(val commentRoute: FeedCommentRoutes, val commentId: Int) {

        @Resource("reply")
        class Reply(val comment: CommentId)
    }
}