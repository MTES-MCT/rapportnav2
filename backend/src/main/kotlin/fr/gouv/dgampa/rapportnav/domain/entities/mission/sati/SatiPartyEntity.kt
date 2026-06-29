package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

data class SatiPartyEntity(
    val id: Int? = null,
    val partyType: String? = null,
    val comments: String? = null,
    val signature: Boolean = false,
    val contact: ContactEntity? = null
)
