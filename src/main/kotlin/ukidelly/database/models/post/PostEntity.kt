package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ukidelly.api.v1.post.models.Currency
import ukidelly.api.v1.post.models.PostCategory
import ukidelly.database.models.user.UserTable

class PostEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<PostEntity>(PostTable)

    var title by PostTable.title
    var content by PostTable.content
    var creator by UserTable.userName
    var creatorIsland by UserTable.islandName
    var category by PostTable.category
    var currency by PostTable.currency
    var price by PostTable.price
    var closed by PostTable.closed
    var createdAt by PostTable.createdAt
    var updatedAt by PostTable.updatedAt

}


// Table
object PostTable : IntIdTable("post", "post_id") {
    val title = varchar("title", 255)
    val content = text("content")
    val userId = reference("user_id", UserTable.id)
    val category = enumerationByName<PostCategory>("category", 100)
    val currency = enumerationByName<Currency>("currency", 100)
    val price = integer("price").nullable()
    val closed = bool("closed")
    val createdAt = varchar("created_at", 255)
    val updatedAt = varchar("updated_at", 255)


}