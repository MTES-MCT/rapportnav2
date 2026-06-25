package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import com.neovisionaries.i18n.CountryCode
import java.time.Instant

data class AddressEntity(
    val id: Int? = null,
    val street: String? = null,
    val fullAddress: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: CountryCode? = null,
    val lng: Double? = null,
    val lat: Double? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
