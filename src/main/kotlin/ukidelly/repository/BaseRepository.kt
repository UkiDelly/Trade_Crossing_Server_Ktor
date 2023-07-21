package ukidelly.repository

import org.bson.Document

interface BaseRepository {
    fun findOne(id: String, email: String): Document?
    fun save()
    fun updateOne()
    fun deleteOne()
}