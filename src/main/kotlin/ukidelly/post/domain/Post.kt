//package ukidelly.post.domain
//
//import kotlinx.serialization.Contextual
//import kotlinx.serialization.Serializable
//import org.bson.codecs.pojo.annotations.BsonId
//import org.bson.types.ObjectId
//import java.time.LocalDateTime
//
//
//@Serializable
//data class PostDetail(
//
//    @Contextual
//    @BsonId val id: ObjectId,
//    val title: String,
//
//    val content: String,
//
//    val creator: String,
//
//    @Contextual
//    val creatorId: ObjectId,
//
//    val creatorIsland: String,
//
//    val category: PostCategory,
//
//    val currency: Currency,
//
//    val price: Int?,
//
//    val closed: Boolean,
//
//    val commentList: List<Comment>,
//
//    @Contextual
//    val createdAt: LocalDateTime,
//)
//
//@Serializable
//data class PostPreview(
//
//    @Contextual
//    @BsonId val id: ObjectId,
//    val title: String,
//
//    val content: String,
//
//    val creator: String,
//
//    val creatorIsland: String,
//
//    val category: PostCategory,
//
//    val currency: Currency,
//
//    val price: Int?,
//
//    val closed: Boolean,
//
//    @Contextual
//    val createdAt: LocalDateTime,
//)
