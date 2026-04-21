package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import com.neovisionaries.i18n.CountryCode
import java.time.Instant
import java.util.UUID

data class Address(
    val id: UUID? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: CountryCode? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val createdAt: Instant? = null
)
