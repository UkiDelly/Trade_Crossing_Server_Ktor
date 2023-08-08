package ukidelly.api.v1.post.service

import org.koin.core.annotation.Module
import ukidelly.api.v1.post.models.PostCreateRequest
import ukidelly.api.v1.post.models.PostDetail
import ukidelly.api.v1.post.models.PostPreview
import ukidelly.api.v1.post.repository.PostRepository
import ukidelly.database.models.comment.CommentDao
import java.util.*


@Module
class PostService {

    private val postRepository = PostRepository
    private val commentDao = CommentDao


    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): List<PostPreview> {
        return postRepository.findLatestPosts(itemsPerPage, page)
    }

    suspend fun getPost(postId: Int): PostDetail? {
        return postRepository.getPost(postId)
    }

    suspend fun addNewPost(newPost: PostCreateRequest, userId: UUID) {
        val postId = postRepository.addNewPost(newPost, userId)
    }
}