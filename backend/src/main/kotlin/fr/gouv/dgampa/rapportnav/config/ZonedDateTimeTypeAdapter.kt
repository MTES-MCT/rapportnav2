package fr.gouv.dgampa.rapportnav.config

import com.google.gson.*
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeTypeAdapter: JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    private  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss z");

    override fun serialize(src: ZonedDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return  JsonPrimitive(formatter.format(src));
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ZonedDateTime {
        return ZonedDateTime.parse(json?.getAsString(), formatter)
    }
}
