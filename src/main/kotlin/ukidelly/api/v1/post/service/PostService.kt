package ukidelly.api.v1.post.service

import org.koin.core.annotation.Module
import ukidelly.api.v1.post.models.PostDto
import ukidelly.database.models.comment.CommentDao
import ukidelly.database.models.post.PostDao
import java.util.*


@Module
class PostService {

    private val postDao = PostDao
    private val commentDao = CommentDao


    suspend fun getLatestPosts(itemsPerPage: Int, page: Int, postId: Int?) {
        postDao.getPosts(itemsPerPage, page, postId)
    }

    suspend fun addNewPost(post: PostDto, userId: UUID) = postDao.addPost(post, userId)
}