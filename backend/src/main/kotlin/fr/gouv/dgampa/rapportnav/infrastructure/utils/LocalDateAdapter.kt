package fr.gouv.dgampa.rapportnav.infrastructure.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString)
    }

    @Throws(JsonParseException::class)
    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
