package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Contact

data class SatiParty(
    val id: Int? = null,
    val comments: String? = null,
    val signature: Boolean = false,
    val contact: Contact? = null,
    val partyType: SatiPartyType? = null
)
