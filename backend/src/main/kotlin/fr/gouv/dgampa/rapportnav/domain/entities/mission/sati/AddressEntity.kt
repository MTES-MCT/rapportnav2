package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import com.neovisionaries.i18n.CountryCode
import java.time.Instant
import java.util.UUID

data class AddressEntity(
    val id: UUID? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val country: CountryCode? = null,
    val createdAt: Instant? = null
)
