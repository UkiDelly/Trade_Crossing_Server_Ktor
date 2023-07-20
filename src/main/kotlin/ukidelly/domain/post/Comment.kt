package ukidelly.domain.post

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Comment(
    @BsonId val id: ObjectId,
    val creatorId: ObjectId,
    val creator: String,
    val creatorIsland: String,
    val postId: ObjectId,
    val reply: List<Comment>
)
