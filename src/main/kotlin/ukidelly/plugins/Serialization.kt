package ukidelly.plugins

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.cfg.EnumFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*


fun Application.configureSerialization() {
    install(ContentNegotiation) {

        
        jackson {
            findAndRegisterModules()
            registerModule(lowerCaseEnumJacksonSerializerModule)
            configure(SerializationFeature.INDENT_OUTPUT, true)
            configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
            configure(JsonGenerator.Feature.IGNORE_UNKNOWN, false)
            configure(EnumFeature.WRITE_ENUMS_TO_LOWERCASE, true)
            configure(EnumFeature.READ_ENUM_KEYS_USING_INDEX, true)
            setDefaultPrettyPrinter(DefaultPrettyPrinter())
            propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE


        }


    }

}

val lowerCaseEnumJacksonSerializerModule = SimpleModule().also {
    val lowerCaseEnumKeySerializer = object : StdSerializer<Enum<*>>(Enum::class.java) {
        override fun serialize(value: Enum<*>?, json: JsonGenerator, provider: SerializerProvider) {
            json.writeFieldName(value?.name?.lowercase())
        }
    }
    val lowerCaseEnumValueSerializer = object : StdSerializer<Enum<*>>(Enum::class.java) {
        override fun serialize(value: Enum<*>?, json: JsonGenerator, provider: SerializerProvider) {
            json.writeString(value?.name?.lowercase())
        }
    }
    it.addKeySerializer(Enum::class.java, lowerCaseEnumKeySerializer)
    it.addSerializer(Enum::class.java, lowerCaseEnumValueSerializer)
}

