package ukidelly.api.v1.feed

import io.ktor.resources.*


@Resource("/feed")
class FeedRoutes {

    @Resource("latest")
    class Latest(val parent: FeedRoutes = FeedRoutes(), val page: Int = 1, val size: Int = 10)

    @Resource("new")
    class New(val parent: FeedRoutes = FeedRoutes())

    @Resource("{feedId}")
    class FeedId(val parent: FeedRoutes = FeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val parent: FeedId)

        @Resource("comment")
        class Comment(val parent: FeedId) {

            @Resource("new")
            class New(val parent: Comment)

            @Resource("{commentId}")
            class CommentId(val parent: Comment, val commentId: Int) {

                @Resource("reply")
                class Reply(val parent: CommentId)
            }
        }
    }
}