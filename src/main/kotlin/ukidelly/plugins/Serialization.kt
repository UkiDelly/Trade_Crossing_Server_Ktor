@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package ukidelly.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bson.types.ObjectId


@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {

        json(
            Json {
                useAlternativeNames = true
                prettyPrint = true
                ignoreUnknownKeys = true
                namingStrategy = JsonNamingStrategy.SnakeCase
                isLenient = true
                serializersModule = SerializersModule {
                    contextual(ObjectIdSerializer)

                }
            }
        )


    }

}


// ObjectId를 직렬화하기 위한 Serializer
@Serializer(forClass = ObjectId::class)
object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ObjectId {

        return ObjectId(decoder.decodeString())
    }
}

