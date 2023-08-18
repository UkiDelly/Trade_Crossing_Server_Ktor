package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PostEntity(
	id: EntityID<Int>
) : IntEntity(id) {
	companion object : IntEntityClass<PostEntity>(PostTable)

	var title by PostTable.title
	var content by PostTable.content
	var userId by PostTable.userId
	var category by PostTable.category
	var currency by PostTable.currency
	var price by PostTable.price
	var closed by PostTable.closed
	var createdAt by PostTable.createdAt
	var updatedAt by PostTable.updatedAt
}