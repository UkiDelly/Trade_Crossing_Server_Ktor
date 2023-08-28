package ukidelly.api.v1.post.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.post.models.PostCreateRequest
import ukidelly.api.v1.post.models.dto.LatestPostDto
import ukidelly.api.v1.post.models.dto.PostDetailDto
import ukidelly.api.v1.post.repository.PostRepository
import ukidelly.database.models.comment.CommentRepository
import java.util.*


@Module
class PostService {

	private val postRepository by inject<PostRepository>(clazz = PostRepository::class.java)
	private val commentRepository = inject<CommentRepository>(clazz = CommentRepository::class.java)

	suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestPostDto {
		val latestPosts = postRepository.findLatestPosts(itemsPerPage, page)
		return LatestPostDto(latestPosts.first, page, latestPosts.second)
	}

	suspend fun getPost(postId: Int): PostDetailDto? {
		val post = postRepository.getPost(postId) ?: return null
		return PostDetailDto(post.first, post.second)
	}

	suspend fun addNewPost(newPost: PostCreateRequest, userId: UUID): PostDetailDto? {
		val newPostId = postRepository.addNewPost(newPost, userId)
		return getPost(newPostId.value)
	}
}