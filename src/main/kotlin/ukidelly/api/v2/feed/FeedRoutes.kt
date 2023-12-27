package ukidelly.api.v2.feed

import io.ktor.resources.*


@Resource("/feeds")
class FeedRoutes(val page: Int = 1, val size: Int = 10) {

    @Resource("{feedId}")
    class FeedId(val parent: FeedRoutes = FeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val parent: FeedId)


    }
}


@Resource("comments")
class FeedCommentRoutes(val parent: FeedRoutes.FeedId) {

    @Resource("new")
    class New(val parent: FeedCommentRoutes)

    @Resource("{commentId}")
    class CommentId(val parent: FeedCommentRoutes, val commentId: Int) {

        @Resource("reply")
        class Reply(val parent: CommentId)
    }
}