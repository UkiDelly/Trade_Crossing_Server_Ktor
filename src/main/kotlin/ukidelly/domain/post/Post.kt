package ukidelly.domain.post

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Post(
    @BsonId val id: ObjectId,
    val title: String,
    val content: String,
    val creatorId: ObjectId,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
    val commentList: List<Comment>,
    val createdAt: LocalDateTime,

    )
