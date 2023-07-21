package ukidelly.repository

import org.bson.Document

interface BaseRepository {
    suspend fun findOne(id: String, email: String): Document?
    suspend fun save(data: Document): Unit
    suspend fun updateOne()
    suspend fun deleteOne()
}