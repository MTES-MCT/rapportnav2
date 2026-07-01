package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Contact

data class SatiParty(
    val id: Int? = null,
    val partyType: String? = null,
    val comments: String? = null,
    val signature: Boolean = false,
    val contact: Contact? = null,
)
