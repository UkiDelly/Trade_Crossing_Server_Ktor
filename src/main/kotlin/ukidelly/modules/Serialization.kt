package ukidelly.modules

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
import kotlinx.serialization.json.JsonNamingStrategy.Builtins.SnakeCase
import kotlinx.serialization.modules.SerializersModule
import java.util.*


@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation) {

        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                namingStrategy = SnakeCase
                serializersModule = SerializersModule {
                    contextual(UUID::class, UUIDSerializerModule)
                }
            }
        )

    }


//        jackson {
//            findAndRegisterModules()
//            registerModule(lowerCaseEnumJacksonSerializerModule)
//            configure(SerializationFeature.INDENT_OUTPUT, true)
//            configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
//            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
//            configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
//            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
//            configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true)
//            configure(JsonGenerator.Feature.IGNORE_UNKNOWN, false)
//            configure(EnumFeature.WRITE_ENUMS_TO_LOWERCASE, true)
//            configure(EnumFeature.READ_ENUM_KEYS_USING_INDEX, true)
//            setDefaultPrettyPrinter(DefaultPrettyPrinter())
//            propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
//
//
//        }
//    }

}


// create a serializer module for UUID
@Serializer(forClass = UUID::class)
object UUIDSerializerModule : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)


    override fun serialize(encoder: Encoder, value: UUID) {

        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

}
