package fr.gouv.dgampa.rapportnav.infrastructure.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime

class GsonSerializer {
    fun create(serializeNulls: Boolean? = false): Gson {
        val builder = GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
            .registerTypeAdapter(Instant::class.java, InstantAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())

        if (serializeNulls == true) {
            builder.serializeNulls()
        }

        return builder.create()
    }
}

