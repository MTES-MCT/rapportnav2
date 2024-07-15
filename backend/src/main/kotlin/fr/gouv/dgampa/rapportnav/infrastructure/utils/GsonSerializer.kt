package fr.gouv.dgampa.rapportnav.infrastructure.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.ZonedDateTime

class GsonSerializer {
    fun create(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
            .create()
    }
}
