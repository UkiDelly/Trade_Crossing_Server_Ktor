package ukidelly.api.v1.feed

import io.ktor.resources.*


@Resource("/feed")
class FeedRoutes {

    @Resource("latest")
    class Latest(val parent: FeedRoutes = FeedRoutes(), val page: Int = 1, val size: Int = 10)

    @Resource("{id}")
    class Id(val parent: FeedRoutes = FeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val parent: Id)

        @Resource("comment")
        class Comment(val parent: Id) {

            @Resource("{id}")
            class Id(val parent: Comment, val commentId: Int) {

                @Resource("reply")
                class Reply(val parent: Id)
            }
        }
    }
}