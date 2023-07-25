package ukidelly.utils

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.SizedIterable
import kotlin.reflect.full.declaredMembers

inline fun <reified Dao : Entity<*>, reified Response> Dao.toResponse(): Response =
    this::class.declaredMembers.let { doaParameters ->
        val response = Response::class
        val responseObjectParameters = Response::class.constructors.first().parameters
        response.constructors.first().call(
            *responseObjectParameters.map { responseParam ->
                doaParameters.find { it.name == responseParam.name }?.call(this)
            }.toTypedArray()
        )
    }

inline fun <reified Dao : Entity<*>, reified Response> SizedIterable<Dao>.toResponse(): List<Response> =
    this.map { doa ->
        doa::class.declaredMembers.let { doaParameters ->
            val response = Response::class
            val responseObjectParameters = Response::class.constructors.first().parameters
            response.constructors.first().call(
                *responseObjectParameters.map { responseParam ->
                    doaParameters.find { it.name == responseParam.name }?.call(doa)
                }.toTypedArray()
            )
        }
    }