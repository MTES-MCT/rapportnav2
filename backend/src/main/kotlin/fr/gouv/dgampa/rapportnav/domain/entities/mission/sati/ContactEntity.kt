package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import com.neovisionaries.i18n.CountryCode
import java.time.Instant
import java.util.UUID

data class ContactEntity(
    val id: UUID? = null,
    val fullName: String? = null,
    val nationality: CountryCode? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: AddressEntity? = null,
    val createdAt: Instant? = null
)
