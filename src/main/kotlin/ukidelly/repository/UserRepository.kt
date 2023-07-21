package ukidelly.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.koin.core.annotation.Module
import org.litote.kmongo.findOne

@Module
class UserRepository : BaseRepository {

    private lateinit var collection: MongoCollection<Document>

    fun setCollection(collection: MongoCollection<Document>) {
        this.collection = collection
    }

    override fun findOne(id: String, email: String): Document? {
        val filter = and(eq("snsId", id), eq("email", email))
        return collection.findOne(filter)
    }

    override fun save() {

    }

    override fun updateOne() {

    }

    override fun deleteOne() {

    }

}