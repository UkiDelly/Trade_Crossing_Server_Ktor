package ukidelly.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.koin.core.annotation.Module
import org.litote.kmongo.findOne
import org.slf4j.LoggerFactory

@Module
class UserRepository : BaseRepository {

    private lateinit var collection: MongoCollection<Document>

    private val logger = LoggerFactory.getLogger("UserRepository")

    fun setCollection(collection: MongoCollection<Document>) {
        this.collection = collection
    }

    override suspend fun findOne(id: String, email: String): Document? {
        val filter = and(eq("snsId", id), eq("email", email))
        return collection.findOne(filter)
    }

    override suspend fun save(data: Document) {
        try {
            collection.insertOne(data)
        } catch (e: Throwable) {
            logger.error("MongoException: {}", e.message)
        }
    }


    override suspend fun updateOne() {
    }

    override suspend fun deleteOne() {

    }

}