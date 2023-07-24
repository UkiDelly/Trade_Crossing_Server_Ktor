//package ukidelly.post.domain
//
//import kotlinx.serialization.Contextual
//import kotlinx.serialization.Serializable
//import org.bson.codecs.pojo.annotations.BsonId
//import org.bson.types.ObjectId
//
//
//@Serializable
//data class Comment(
//    @Contextual
//    @BsonId val id: ObjectId,
//
//    @Contextual
//    val creatorId: ObjectId,
//
//    val creator: String,
//
//    val creatorIsland: String,
//
//    @Contextual
//    val postId: ObjectId,
//
//    val reply: List<Comment>
//)
